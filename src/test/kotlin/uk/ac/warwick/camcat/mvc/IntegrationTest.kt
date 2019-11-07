package uk.ac.warwick.camcat.mvc

import com.gargoylesoftware.htmlunit.WebClient
import org.junit.Before
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc
import uk.ac.warwick.camcat.context.ContextTest

@AutoConfigureMockMvc
@AutoConfigureWebClient
abstract class IntegrationTest : ContextTest() {
  @Autowired
  final lateinit var mvc: MockMvc

  @Autowired
  final lateinit var webClient: WebClient

  @Before
  fun disableJavaScript() {
    webClient.options.isJavaScriptEnabled = false
  }
}
