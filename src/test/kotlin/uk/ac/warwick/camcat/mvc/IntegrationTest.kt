package uk.ac.warwick.camcat.mvc

import com.gargoylesoftware.htmlunit.WebClient
import org.junit.Before
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc
import uk.ac.warwick.camcat.context.ContextTest
import uk.ac.warwick.camcat.search.services.ModuleSearchIndexingService

@AutoConfigureMockMvc
@AutoConfigureWebClient
abstract class IntegrationTest : ContextTest() {
  @Autowired
  final lateinit var mvc: MockMvc

  @Autowired
  final lateinit var webClient: WebClient

  @Autowired
  lateinit var moduleSearchIndexingService: ModuleSearchIndexingService

  @Before
  fun disableJavaScript() {
    webClient.options.isJavaScriptEnabled = false
  }

  @Before
  fun indexModules() {
    moduleSearchIndexingService.indexModules()
  }
}
