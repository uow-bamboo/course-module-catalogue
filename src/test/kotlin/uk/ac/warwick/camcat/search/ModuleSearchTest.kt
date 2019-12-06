package uk.ac.warwick.camcat.search

import org.junit.Assert.assertEquals
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.ac.warwick.camcat.mvc.IntegrationTest
import uk.ac.warwick.camcat.search.queries.ModuleQuery
import uk.ac.warwick.camcat.search.services.ModuleSearchService
import uk.ac.warwick.util.termdates.AcademicYear

class ModuleSearchTest : IntegrationTest() {
  @Autowired
  lateinit var moduleSearchService: ModuleSearchService

  @Test
  fun testQuery() {
    val results =
      moduleSearchService.query(ModuleQuery(keywords = "CS126-15", academicYear = AcademicYear.starting(2020)))

    assertEquals(1, results.page.content.size)

    val result = results.page.content.first()!!

    assertEquals(2020, result.academicYear)
    assertEquals("CS126-15", result.code)
    assertEquals("Design of Information Structures", result.title)
  }

  @Test
  fun testNoResultsWhenNoOccurrence() {
    val results =
      moduleSearchService.query(ModuleQuery(keywords = "CS126-15", academicYear = AcademicYear.starting(2021)))

    assertEquals(0, results.page.content.size)
  }
}
