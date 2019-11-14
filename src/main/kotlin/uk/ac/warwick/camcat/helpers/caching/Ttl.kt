package uk.ac.warwick.camcat.helpers.caching

import java.time.Duration

object Ttl {
  /**
   * If your value is nullable, uses one TTL for non-null values and
   * another TTL for null. Useful for caching not-found results for a shorter time.
   */
  fun <A> nullableStrategy(nonNull: Duration, isNull: Duration): (A?) -> Duration = {
    if (it != null) nonNull
    else isNull
  }
}
