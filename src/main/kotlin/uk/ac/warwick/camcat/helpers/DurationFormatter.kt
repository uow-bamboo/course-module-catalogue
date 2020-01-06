package uk.ac.warwick.camcat.helpers

import humanize.Humanize.pluralize
import java.time.Duration
import java.time.Period

object DurationFormatter {
  fun format(duration: Duration): String {
    val seconds = duration.seconds
    val minutes = (seconds / 60) % 60
    val hours = seconds / 3600

    return(
      pluralize("1 hour", "{0} hours", "", hours) +
      pluralize(" 1 minute", " {0} minutes", "", minutes)
    ).trim()
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
