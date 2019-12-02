package uk.ac.warwick.camcat.sits.services

import org.hamcrest.Matchers.contains
import org.junit.Assert.*
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.ac.warwick.camcat.context.ContextTest
import uk.ac.warwick.util.termdates.AcademicYear

class ModuleServiceTest : ContextTest() {
  @Autowired
  private lateinit var moduleService: ModuleService

  @Test
  fun testFindModuleByModuleCode() {
    val module = moduleService.findByModuleCode("CS261-15")

    assertNotNull(module)
    module!!

    assertEquals("CS261-15", module.code)
    assertEquals("Software Engineering", module.title)
  }

  @Test
  fun testFindRelatedModules() {
    val rules = moduleService.findRelatedModules("CS261-15", AcademicYear.starting(2020))

    assertThat(rules.preRequisites.map { it.code }, contains("CS118-15", "CS126-15"))
  }
}
