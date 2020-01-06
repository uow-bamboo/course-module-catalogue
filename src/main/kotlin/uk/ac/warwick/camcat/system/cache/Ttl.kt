package uk.ac.warwick.camcat.system.cache

import java.time.Duration

object Ttl {
  /**
   * If your value is nullable, uses one TTL for non-null values and
   * another TTL for null. Useful for caching not-found results for a shorter time.
   */
  private fun nullableStrategy(nonNull: Duration, isNull: Duration): (Any?) -> Duration = {
    if (it != null) nonNull
    else isNull
  }

  fun default(): (Any?) -> Duration = nullableStrategy(
    nonNull = Duration.ofMinutes(20),
    isNull = Duration.ofMinutes(5)
  )
}
