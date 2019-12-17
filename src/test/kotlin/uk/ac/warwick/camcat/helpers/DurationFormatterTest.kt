package uk.ac.warwick.camcat.helpers

import org.junit.Assert.assertEquals
import org.junit.Test
import uk.ac.warwick.camcat.helpers.DurationFormatter.format
import java.time.Duration

class DurationFormatterTest {
  @Test
  fun test() {
    assertEquals("", format(Duration.ZERO))

    assertEquals("1 minute", format(Duration.ofMinutes(1L)))
    assertEquals("2 minutes", format(Duration.ofMinutes(2L)))

    assertEquals("1 hour", format(Duration.ofMinutes(60L)))
    assertEquals("1 hour 1 minute", format(Duration.ofMinutes(61L)))
    assertEquals("1 hour 2 minutes", format(Duration.ofMinutes(62L)))

    assertEquals("2 hours", format(Duration.ofMinutes(120L)))
    assertEquals("2 hours 1 minute", format(Duration.ofMinutes(121L)))
    assertEquals("2 hours 2 minutes", format(Duration.ofMinutes(122L)))

    assertEquals("48 hours", format(Duration.ofHours(48L)))
  }
}
