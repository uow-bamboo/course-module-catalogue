package uk.ac.warwick.camcat.helpers.caching

import org.springframework.cache.Cache
import uk.ac.warwick.camcat.system.Logging
import java.lang.ClassCastException
import java.time.Duration
import java.time.Instant

class VariableTtlCacheHelper<A>(
  private val cache: Cache,
  private val ttlStrategy: (A) -> Duration,
  private val classOf: Class<out A>
) : Logging() {

  fun getOrElseUpdate(key: String, update: () -> A): A =
    getOrElseUpdateElement(key, update).value

  fun getOrElseUpdateElement(key: String, update: () -> A): CacheElement<A> {
    @Suppress("UNCHECKED_CAST")
    fun validateCachedValueType(element: CacheElement<*>): CacheElement<A> =
      if (element.value == null || classOf == element.value.javaClass) element as CacheElement<A>
      else {
        logger.info("Incorrect type from cache fetching $key; doing update")
        doSet(key, update())
      }

    try {
      val existing: CacheElement<*>? = cache[key]?.get() as CacheElement<*>
      return when {
        existing?.isStale() == true ->
          // try to get a fresh value but return the cached value if that fails
          try {
            doSet(key, update())
          } catch (t: Throwable) {
            logger.error("Unable to fetch fresh value for $key failed. Returning stale value", t)
            validateCachedValueType(existing)
          }
        existing != null -> validateCachedValueType(existing)
        else -> doSet(key, update())
      }
    } catch (t: ClassCastException) {
      logger.error("Unable to cast existing cache value to CacheElement; doing update", t)
      return doSet(key, update())
    }
  }

  private fun doSet(key: String, value: A): CacheElement<A> {
    val ttl = ttlStrategy(value)
    val now = Instant.now()
    val softExpiry = now.plus(ttl).epochSecond
    val element = CacheElement(value, now.epochSecond, softExpiry)
    cache.put(key, element)
    return element
  }

}
