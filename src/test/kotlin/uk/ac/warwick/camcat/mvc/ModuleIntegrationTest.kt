package uk.ac.warwick.camcat.mvc

import com.gargoylesoftware.htmlunit.html.HtmlAnchor
import com.gargoylesoftware.htmlunit.html.HtmlPage
import com.gargoylesoftware.htmlunit.html.HtmlTable
import org.hamcrest.Matchers.*
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.get
import uk.ac.warwick.camcat.system.security.Role
import uk.ac.warwick.userlookup.User
import uk.ac.warwick.userlookup.UserLookupInterface

class ModuleIntegrationTest : IntegrationTest() {
  @Autowired
  lateinit var userLookup: UserLookupInterface

  @Before
  fun setUp() {
    val user = User()
    user.fullName = "John Smith"

    `when`(userLookup.getUserByWarwickUniId("0000126")).thenReturn(user)
  }

  @Test
  @WithMockUser(roles = [Role.user])
  fun test() {
    val page: HtmlPage = webClient.getPage("http://localhost/modules/2019/CS126-15")

    fun content(id: String): String = page.getElementById(id).textContent
    fun tableCells(tableId: String): List<String> =
      (page.getHtmlElementById(tableId) as HtmlTable).bodies.first().rows.flatMap { it.cells }
        .map { it.textContent.trim() }

    assertThat(content("module-code"), equalToCompressingWhiteSpace("CS126-15"))
    assertThat(content("module-title"), equalToCompressingWhiteSpace("Design of Information Structures"))
    assertThat(content("module-department-name"), equalToCompressingWhiteSpace("Computer Science"))
    assertThat(content("module-faculty-name"), equalToCompressingWhiteSpace("Science"))
    assertThat(content("module-level-name"), equalToCompressingWhiteSpace("Undergraduate Level 1"))
    assertThat(content("module-leader"), equalToCompressingWhiteSpace("John Smith"))
    assertThat(content("module-credit-value"), equalToCompressingWhiteSpace("15"))
    assertThat(
      (page.getHtmlElementById("module-web-page") as HtmlAnchor).hrefAttribute,
      equalTo("https://warwick.ac.uk/fac/sci/dcs/teaching/material/cs126/")
    )

    assertThat(
      content("module-aims"),
      stringContainsInOrder("standard abstract data types (ADTs) such as linked lists")
    )

    assertThat(
      content("module-pre-requisite-modules"),
      equalToCompressingWhiteSpace("CS118-15 Programming for Computer Scientists")
    )

    assertThat(
      content("module-post-requisite-modules"),
      equalToCompressingWhiteSpace("CS261-15 Software Engineering")
    )

    assertThat(
      tableCells("module-teaching-split"),
      containsInRelativeOrder(
        "Computer Science", "80%",
        "Warwick Mathematics Institute", "20%"
      )
    )

    assertThat(
      tableCells("module-study-time"),
      containsInRelativeOrder(
        "Lectures", "30 sessions of 1 hour",
        "Practical classes", "8 sessions of 2 hours",
        "Private study", "20 hours"
      )
    )
    assertThat(
      content("module-total-study-hours"),
      equalToCompressingWhiteSpace("66 hours")
    )

    assertThat(
      tableCells("module-assessment"),
      containsInRelativeOrder(
        "1000 word essay", "Essay", "50%",
        "Students write an essay",
        "2 hour exam", "Examination - Summer", "50%",
        "Students sit an exam"
      )
    )

    assertThat(
      content("module-core-availability"),
      stringContainsInOrder("Year 1 of", "UCSA-G500", "Computer Science")
    )
  }

  @Test
  @WithMockUser(roles = [Role.user])
  fun testNotFoundWhenNoOccurrence() {
    mvc.get("/modules/2030/CS126-15").andExpect {
      status { isNotFound }
    }
  }

  @Test
  @WithMockUser(roles = [Role.user])
  fun testNotFoundWhenModuleCodeDoesNotExist() {
    mvc.get("/modules/2020/AB123-45").andExpect {
      status { isNotFound }
    }
  }
}
