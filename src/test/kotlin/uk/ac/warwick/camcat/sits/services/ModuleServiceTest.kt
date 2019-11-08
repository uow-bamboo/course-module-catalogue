package uk.ac.warwick.camcat.sits.services

import org.hamcrest.Matchers.*
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
  fun testFindModuleRules() {
    val rules = moduleService.findRules("CS261-15", AcademicYear.starting(2019))
    assertThat(rules, hasSize(2))

    val prerequisites = rules.elementAt(0)
    assertEquals("To take CS261-15 you must have taken and passed CS118-15 and CS126-15", prerequisites.description)
    assertEquals("COMPULSORY", prerequisites.ruleClass?.name)
    assertEquals("PRE-REQUISITE", prerequisites.type?.name)

    val preReqModules = prerequisites.elements
      .map{ eb -> eb.formedModuleCollection }
      .flatMap{ fmc -> fmc?.formedModuleCollectionElements?.asIterable() ?: emptyList() }
      .mapNotNull{ e -> e.module?.code }

    assertEquals(listOf("CS118-15", "CS126-15"), preReqModules)
  }
}
