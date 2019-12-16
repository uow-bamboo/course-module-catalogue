package uk.ac.warwick.camcat.mvc

import com.gargoylesoftware.htmlunit.html.*
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.Assert.assertThat
import org.junit.Test
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.get
import uk.ac.warwick.camcat.system.security.Role

class HomeIntegrationTest : IntegrationTest() {
  @Test
  @WithMockUser(roles = [Role.user])
  fun homeSignedIn() {
    mvc.get("/").andExpect {
      status { isOk }
      content { string(containsString("Or, browse by department:")) }
    }
  }

  @Test
  @WithAnonymousUser
  fun homeSignedOut() {
    mvc.get("/").andExpect {
      status { isOk }
    }
  }

  @Test
  @WithMockUser(roles = [Role.user])
  fun linkToViewByDepartmentPage() {
    var page: HtmlPage = webClient.getPage("http://localhost")
    val links = page.anchors.filter { it.textContent.trim() == "Computer Science" }
    assertThat(links, hasSize(1))
    page = links.first().click()

    var moduleRows = emptyList<HtmlTableRow>()
    var csModuleRows = emptyList<HtmlTableRow>()
    var selectedFilterOptions = emptyList<HtmlOption>()

    fun getPageResult() {
      moduleRows = page.getByXPath<HtmlTableRow>("//tr[contains(@class, 'module')]")
      csModuleRows = moduleRows.filter { it.getElementsByTagName("td").last().textContent.trim() == "DCS" }
      selectedFilterOptions = page.getByXPath<HtmlOption>("//option[@selected]")
    }

    // 31 dcs modules across 2 pages
    // ensure entire first page is DCS modules
    getPageResult()
    assertThat(moduleRows, hasSize(20))
    assertThat(csModuleRows, hasSize(moduleRows.size))
    assertThat(selectedFilterOptions, hasSize(1))
    assertThat(selectedFilterOptions.first().textContent, equalTo("DCS"))

    fun getLastPage() {
      val pageButtons = page
        .getByXPath<HtmlUnorderedList>("//ul[contains(@class, 'pagination')]")
        .first().getByXPath<HtmlButton>("//button")
      val lastPageNumber = pageButtons[pageButtons.size - 2].valueAttribute
      page = webClient.getPage("http://localhost/modules/?departments=CS&page=${lastPageNumber}}")
    }

    // ensure 2nd page is also entirely DCS
    // pageButton.click() does not work, hence we are manually navigating to 2nd page
    getLastPage()
    getPageResult()
    assertThat(csModuleRows, hasSize(moduleRows.size))
    assertThat(selectedFilterOptions, hasSize(1))
    assertThat(selectedFilterOptions.first().textContent, equalTo("DCS"))
  }
}
