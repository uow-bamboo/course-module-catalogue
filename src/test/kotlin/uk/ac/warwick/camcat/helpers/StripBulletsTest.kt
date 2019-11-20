package uk.ac.warwick.camcat.helpers

import org.junit.Assert.assertEquals
import org.junit.Test
import uk.ac.warwick.camcat.helpers.StripBullets.replaceBullets
import uk.ac.warwick.camcat.helpers.StripBullets.stripBullets

class StripBulletsTest {
  @Test
  fun testStripBullets() {
    assertEquals("One\nTwo", stripBullets("• One\n• Two"))
    assertEquals("One\nTwo", stripBullets("\t•\tOne\n\t•\tTwo"))
  }

  @Test
  fun testReplaceBullets() {
    val sample = "·\tTypes and their properties\n·\tAbstract data types\n·\tAlgorithms"
    val expected = "- Types and their properties\n- Abstract data types\n- Algorithms"

    assertEquals(expected, replaceBullets(sample, with = "-"))
  }
}
