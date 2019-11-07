package uk.ac.warwick.camcat.templates.helpers

import org.junit.Test
import uk.ac.warwick.camcat.services.NavigationDropdown
import uk.ac.warwick.camcat.services.NavigationPage
import uk.ac.warwick.camcat.services.NavigationPresenter
import uk.ac.warwick.camcat.templates.TemplateTest

class NavTest : TemplateTest() {
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
