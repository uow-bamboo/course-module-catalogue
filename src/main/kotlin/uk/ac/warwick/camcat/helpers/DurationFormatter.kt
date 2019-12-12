package uk.ac.warwick.camcat.helpers

import humanize.Humanize.pluralize
import java.time.Duration

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
}
