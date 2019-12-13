package uk.ac.warwick.camcat.helpers

import java.math.BigDecimal
import java.time.Duration
import java.time.Period

object DurationFormatter {
  fun format(duration: Duration): String {
    val seconds = BigDecimal(duration.seconds)
    val minutes = seconds.divide(BigDecimal(60)).rem(BigDecimal(60))
    val hours = seconds.divide(BigDecimal(3600))

    var string = ""

    if (hours != BigDecimal.ZERO) {
      string += "$hours ${if (hours == BigDecimal.ONE) "hour" else "hours"} "
    }

    if (minutes != BigDecimal.ZERO) {
      string += "$minutes ${if (minutes == BigDecimal.ONE) "minute" else "minutes"} "
    }

    return string.trimEnd()
  }

  fun durationInDaysOrWeeks(code: String): String? = Period.parse(code)?.days?.let { d ->
    return if (d == 1) {
      "1 day"
    } else if (d == 7) {
      "1 week"
    } else if (d % 7 == 0) {
      "${d / 7} weeks"
    } else "$d days"
  }

}
