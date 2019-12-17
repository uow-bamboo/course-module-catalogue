package uk.ac.warwick.camcat.system.cache

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.Instant

@JsonIgnoreProperties(ignoreUnknown = true)
data class CacheElement(
  val value: String?,
  val created: Long,
  val softExpiry: Long
) {
  val stale: Boolean
    get() = Instant.now().epochSecond > softExpiry
}
