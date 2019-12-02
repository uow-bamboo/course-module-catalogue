package uk.ac.warwick.camcat.sits.repositories

import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.empty
import org.junit.Assert.assertThat
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.ac.warwick.camcat.context.ContextTest
import uk.ac.warwick.util.termdates.AcademicYear

class AssessmentComponentRepositoryTest : ContextTest() {
  @Autowired
  lateinit var repository: AssessmentComponentRepository

  private fun components(academicYear: AcademicYear) =
    repository.findAllByAssessmentPatternCodeAndAcademicYear("CS126-15", academicYear)
      .map { it.key.sequence }

  /**
   * No components are returned when none have yet been introduced through the module approval process.
   */
  @Test
  fun testNoComponents() {
    assertThat(components(AcademicYear.starting(2019)), empty())
  }

  /**
   * Assessment components introduced for 20/21 are returned for 20/21.
   */
  @Test
  fun testComponentsIntroducedInAcademicYear() {
    assertThat(components(AcademicYear.starting(2020)), contains("A01", "E01"))
  }

  /**
   * Assessment components introduced for 20/21 are returned for 21/22, when there are no components
   * introduced for 21/22.
   */
  @Test
  fun testComponentsIntroducedInPreviousAcademicYear() {
    assertThat(components(AcademicYear.starting(2021)), contains("A01", "E01"))
  }

  /**
   * Assessment components introduced for 22/23 are returned for 22/23, even though they are not in use.
   */
  @Test
  fun testNotInUseComponentsIntroducedInFutureAcademicYear() {
    assertThat(components(AcademicYear.starting(2022)), contains("A03", "A04", "E02"))
  }
}
