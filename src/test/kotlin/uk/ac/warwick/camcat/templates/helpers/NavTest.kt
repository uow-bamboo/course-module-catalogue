package uk.ac.warwick.camcat.templates.helpers

import freemarker.template.Configuration
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import uk.ac.warwick.camcat.services.NavigationDropdown
import uk.ac.warwick.camcat.services.NavigationPage
import uk.ac.warwick.camcat.services.NavigationPresenter
import uk.ac.warwick.camcat.templates.FreeMarkerRendering

@RunWith(SpringRunner::class)
@ActiveProfiles("test")
@SpringBootTest
class NavTest : FreeMarkerRendering {
  @Autowired
  override lateinit var configuration: Configuration

  @Test
  fun testRenders() {
    val template = "<#import '*/helpers/nav.ftlh' as nav>" +
      "<@nav.nav navigation />"

    val model = mapOf(
      "navigation" to NavigationPresenter(
        "/", listOf(
        NavigationPage("Home", "/"),
        NavigationDropdown(
          "Dropdown", "/dropdown", listOf(
          NavigationPage("Item one", "/dropdown/one"),
          NavigationPage("Item two", "/dropdown/two")
        )
        ),
        NavigationPage(
          "Parent page", "/parent", listOf(
          NavigationPage(
            "Child page", "/parent/child", listOf(
            NavigationPage("Sub-child page", "/parent/child/child")
          )
          )
        )
        )
      )
      )
    )

    renderTemplateString(template, model)
  }
}
