package uk.ac.warwick.camcat.sits.services

import org.junit.Assert.*
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.ac.warwick.camcat.context.ContextTest

class ModuleServiceTest : ContextTest() {
  @Autowired
  private lateinit var moduleService: ModuleService

  @Test
  fun testFindModuleByModuleCode() {
    val module = moduleService.findByModuleCode("CS118-15")

    assertNotNull(module)
    module!!

    assertEquals("CS118-15", module.code)
    assertEquals("Programming for Computer Scientists", module.name)
    assertTrue(module.inUse!!)
  }
}
