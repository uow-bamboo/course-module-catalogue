package uk.ac.warwick.camcat.system.cache

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.cache.Cache
import uk.ac.warwick.camcat.system.Logging
import java.time.Duration
import java.time.Instant

class VariableTtlCacheDecorator(val cache: Cache, val objectMapper: ObjectMapper) : Logging() {
  inline fun <reified T> get(key: Any, valueLoader: () -> T?): T? =
    get(key, Ttl.default(), valueLoader)

  inline fun <reified T> get(key: Any, ttl: (T?) -> Duration, valueLoader: () -> T?): T? {
    val cached = cache.get(key, String::class.java)?.let { objectMapper.readValue<CacheElement>(it) }

    if (cached == null) {
      try {
        val computed = valueLoader.invoke()
        put(key, computed, ttl)
        return computed
      } catch (e: Throwable) {
        logger.info("Exception computing value for key $key", e)
        throw e
      }
    }

    if (cached.stale) {
      try {
        val computed = valueLoader.invoke()
        put(key, computed, ttl)
        logger.info("Refreshed stale cache entry for $key")
        return computed
      } catch (e: Throwable) {
        logger.info("Exception computing new value for key $key; returning stale value", e)
      }
    }

    return cached.value?.let(objectMapper::readValue)
  }

  inline fun <reified T> put(key: Any, value: T?, ttl: (T?) -> Duration) {
    val cacheElement = CacheElement(
      objectMapper.writeValueAsString(value),
      Instant.now().epochSecond,
      Instant.now().plus(ttl(value)).epochSecond
    )

    cache.put(key, objectMapper.writeValueAsString(cacheElement))
  }
}
