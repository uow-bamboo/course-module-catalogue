package uk.ac.warwick.camcat.mvc

import com.gargoylesoftware.htmlunit.TextPage
import com.gargoylesoftware.htmlunit.html.*
import org.hamcrest.Matchers.*
import org.junit.Assert.assertThat
import org.junit.Test
import org.springframework.security.test.context.support.WithMockUser
import uk.ac.warwick.camcat.system.security.Role

class ModulesIntegrationTest : IntegrationTest() {
  @Test
  @WithMockUser(roles = [Role.user])
  fun testFindModuleByText() {
    var page: HtmlPage = webClient.getPage("http://localhost")
    var form = page.getFormByName("modules")

    form.getInputByName<HtmlTextInput>("keywords").valueAttribute = "abstract data types"

    page = form.getElementsByAttribute<HtmlButton>("button", "type", "submit").first().click()
    form = page.getFormByName("modules")

    assertThat(form.getInputByName<HtmlTextInput>("keywords"), hasProperty("valueAttribute", equalTo("abstract data types")))

    val links = page.anchors.filter { it.textContent.contains("CS126-15") }

    assertThat(links, hasSize(1))

    page = links.first().click()

    val headings = page.getElementsByTagName("h1").filter { it.textContent.contains("Design of Information Structures") }

    assertThat(headings, hasSize(1))
  }

  /**
   * Fill out each field on the form, then submit the form, then check that the fields are re-populated.
   */
  @Test
  @WithMockUser(roles = [Role.user])
  fun testSearchForm() {
    var page: HtmlPage = webClient.getPage("http://localhost/modules")
    var form = page.getFormByName("modules")

    form.getSelectByName("departments").setSelectedAttribute<HtmlPage>("CS", true)
    form.getSelectByName("levels").setSelectedAttribute<HtmlPage>("1", true)
    form.getSelectByName("creditValues").setSelectedAttribute<HtmlPage>("15", true)
    form.getSelectByName("assessmentTypes").setSelectedAttribute<HtmlPage>("ES", true)

    page = form.getElementsByAttribute<HtmlButton>("button", "type", "submit").first().click()
    form = page.getFormByName("modules")

    assertThat(
      form.getSelectByName("levels").selectedOptions,
      hasItem(hasProperty("text", equalTo("Undergraduate Level 1")))
    )
    assertThat(
      form.getSelectByName("departments").selectedOptions,
      hasItem(hasProperty("text", equalTo("DCS")))
    )
    assertThat(
      form.getSelectByName("creditValues").selectedOptions,
      hasItem(hasProperty("text", equalTo("15")))
    )
    assertThat(
      form.getSelectByName("assessmentTypes").selectedOptions,
      hasItem(hasProperty("text", equalTo("Examination - Summer")))
    )
  }

  @Test
  @WithMockUser(roles = [Role.user])
  fun testFindModuleByCode() {
    var page: HtmlPage = webClient.getPage("http://localhost")

    val form = page.getFormByName("modules")
    form.getInputByName<HtmlTextInput>("keywords").valueAttribute = "CS126"

    page = form.getElementsByAttribute<HtmlButton>("button", "type", "submit").first().click()

    val links = page.anchors.filter { it.textContent.contains("CS126-15") }

    assertThat(links, hasSize(1))

    page = links.first().click()

    val headings = page.getElementsByTagName("h1").filter { it.textContent.contains("Design of Information Structures") }

    assertThat(headings, hasSize(1))
  }

  @Test
  @WithMockUser(roles = [Role.user])
  fun testPagination() {
    var page: HtmlPage = webClient.getPage("http://localhost/modules")

    var pageButtons = page
      .getByXPath<HtmlUnorderedList>("//ul[contains(@class, 'pagination')]")
      .first()
      .getElementsByTagName("button")

    assertThat(pageButtons.map { it.textContent.trim() }, containsInRelativeOrder("«", "1", "2", "»"))

    // 20 modules per page, 2 links per module
    assertThat(page.getByXPath<HtmlTableRow>("//tr[contains(@class, 'module')]"), hasSize(20))

    // previous button should be disabled on first page
    assert(pageButtons.first().hasAttribute("disabled"))

    // next button should not be disabled on first page
    assert(!pageButtons.last().hasAttribute("disabled"))

    // button for current page should have active class
    assert(pageButtons[1].getAttribute("class").contains("active"))

    // page 1 should have value of 0
    assert(pageButtons[1].getAttribute("value") == "0")
    assert(pageButtons[1].textContent.trim() == "1")

    // go to last page
    // page = pageButtons[2].click() // this does not work for unknown reason
    page = webClient.getPage("http://localhost/modules/?page=1")

    pageButtons = page
      .getByXPath<HtmlUnorderedList>("//ul[contains(@class, 'pagination')]")
      .first()
      .getElementsByTagName("button")

    // 10 modules on last page
    assertThat(page.getByXPath<HtmlTableRow>("//tr[contains(@class, 'module')]"), hasSize(12))
    assert(pageButtons.last().hasAttribute("disabled")) // next button should be disabled on last page
    assert(!pageButtons.first().hasAttribute("disabled")) // previous button should not be disabled on last page
  }

  @Test
  @WithMockUser(roles = [Role.user])
  fun testRedirectToModulePageIfUserInputModuleCodeAndGotUniqueResult() {
    // upper case search through form
    var page: HtmlPage = webClient.getPage("http://localhost")
    var form = page.getFormByName("modules")
    form.getInputByName<HtmlTextInput>("keywords").valueAttribute = "CS126-15"
    page = form.getElementsByAttribute<HtmlButton>("button", "type", "submit").first().click()
    assertThat(page.url.path, equalTo("/modules/2020/CS126-15"))

    // lower case search through form
    page = webClient.getPage("http://localhost")
    form = page.getFormByName("modules")
    form.getInputByName<HtmlTextInput>("keywords").valueAttribute = "cs126-15"
    page = form.getElementsByAttribute<HtmlButton>("button", "type", "submit").first().click()
    assertThat(page.url.path, equalTo("/modules/2020/CS126-15"))

    // upper case with direct url
    page = webClient.getPage("http://localhost/modules?keywords=CS126-7.5")
    assertThat(page.url.path, equalTo("/modules/2020/CS126-7.5"))

    // lower case with direct url
    page = webClient.getPage("http://localhost/modules?keywords=cs126-7.5")
    assertThat(page.url.path, equalTo("/modules/2020/CS126-7.5"))

    // with unrelated filter value
    page = webClient.getPage("http://localhost/modules?keywords=cs126-7.5&departments=SSS")
    assertThat(page.url.path, equalTo("/modules/2020/CS126-7.5"))

    // with incorrect module code it shoule go 404
    webClient.options.isThrowExceptionOnFailingStatusCode = false;
    val unexpectedPage: TextPage = webClient.getPage("http://localhost/modules?keywords=cs765-7.5")
    assertThat(unexpectedPage.webResponse.statusCode, equalTo(404))
  }

  @Test
  @WithMockUser(roles = [Role.user])
  fun testNotRedirectWithUniqueResultButNonModuleCodeKeywordsInput() {
    val page: HtmlPage = webClient.getPage("http://localhost/modules?keywords=abstract%20data%20types")
    assertThat(page.url.toString(), equalTo("http://localhost/modules?keywords=abstract%20data%20types"))
  }

}
