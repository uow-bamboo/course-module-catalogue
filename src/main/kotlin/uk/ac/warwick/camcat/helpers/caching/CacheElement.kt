package uk.ac.warwick.camcat.helpers.caching

import java.io.Serializable
import java.time.Instant

data class CacheElement<V>(
  val value: V,
  val created: Long,
  val softExpiry: Long
) : Serializable {
  fun isStale(): Boolean = Instant.now().epochSecond > softExpiry
}
