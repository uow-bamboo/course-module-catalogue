package uk.ac.warwick.camcat.mvc

import com.gargoylesoftware.htmlunit.html.HtmlButton
import com.gargoylesoftware.htmlunit.html.HtmlPage
import com.gargoylesoftware.htmlunit.html.HtmlTextInput
import org.hamcrest.Matchers.*
import org.junit.Assert.assertThat
import org.junit.Test
import org.springframework.security.test.context.support.WithMockUser
import uk.ac.warwick.camcat.system.security.Role

class ModulesIntegrationTest : IntegrationTest() {
  /**
   * Fill out each field on the form, then submit the form, then check that the fields are re-populated.
   */
  @Test
  @WithMockUser(roles = [Role.user])
  fun form() {
    var page: HtmlPage = webClient.getPage("http://localhost/modules")
    var form = page.getFormByName("modules")

    form.getInputByName<HtmlTextInput>("code").valueAttribute = "CS1"
    form.getInputByName<HtmlTextInput>("keywords").valueAttribute = "Java"
    form.getSelectByName("department").setSelectedAttribute<HtmlPage>("CS", true)
    form.getSelectByName("level").setSelectedAttribute<HtmlPage>("1", true)
    form.getSelectByName("faculty").setSelectedAttribute<HtmlPage>("S", true)
    form.getSelectByName("academicYear").setSelectedAttribute<HtmlPage>("2019", true)
    form.getInputsByName("assessmentTypes").find { it.valueAttribute == "ES" }?.isChecked = true

    page = form.getElementsByAttribute<HtmlButton>("button", "type", "submit").first().click()
    form = page.getFormByName("modules")

    assertThat(form.getInputByName<HtmlTextInput>("code"), hasProperty("valueAttribute", equalTo("CS1")))
    assertThat(form.getInputByName<HtmlTextInput>("keywords"), hasProperty("valueAttribute", equalTo("Java")))
    assertThat(
      form.getSelectByName("level").selectedOptions,
      hasItem(hasProperty("text", equalTo("Undergraduate Level 1")))
    )
    assertThat(
      form.getSelectByName("department").selectedOptions,
      hasItem(hasProperty("text", equalTo("Computer Science")))
    )
    assertThat(
      form.getSelectByName("faculty").selectedOptions,
      hasItem(hasProperty("text", equalTo("Science")))
    )
    assertThat(
      form.getSelectByName("academicYear").selectedOptions,
      hasItem(hasProperty("text", equalTo("19/20")))
    )
    assertThat(
      form.getInputsByName("assessmentTypes").filter { it.isChecked },
      hasItem(hasProperty("valueAttribute", equalTo("ES")))
    )
  }

  @Test
  @WithMockUser(roles = [Role.user])
  fun testFindModuleByCode() {
    var page: HtmlPage = webClient.getPage("http://localhost/modules")

    val form = page.getFormByName("modules")
    form.getInputByName<HtmlTextInput>("code").valueAttribute = "CS126"
    form.getSelectByName("academicYear").setSelectedAttribute<HtmlPage>("2019", true)

    page = form.getElementsByAttribute<HtmlButton>("button", "type", "submit").first().click()

    val links = page.anchors.filter { it.textContent.contains("CS126-15") }

    assertThat(links, hasSize(1))

    page = links.first().click()

    val headings = page.getElementsByTagName("h1").filter { it.textContent.contains("Design of Information Structures") }

    assertThat(headings, hasSize(1))
  }
}
