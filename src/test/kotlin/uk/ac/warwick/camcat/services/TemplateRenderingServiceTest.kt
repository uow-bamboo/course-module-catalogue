package uk.ac.warwick.camcat.services

import org.junit.Assert.assertEquals
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.ac.warwick.camcat.context.ContextTest

class TemplateRenderingServiceTest : ContextTest() {
  @Autowired
  lateinit var service: TemplateRenderingService

  @Test
  fun renders() {
    val html = service.render("test", mapOf("name" to "Course and Module Catalogue"))

    assertEquals("<p>Hello from Course and Module Catalogue</p>", html.trim())
  }
}
