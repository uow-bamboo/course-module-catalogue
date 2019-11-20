package uk.ac.warwick.camcat.services

import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import org.springframework.stereotype.Service
import uk.ac.warwick.camcat.helpers.StripBullets.stripBullets

interface MarkdownService {
  fun render(markdown: String): String
}

@Service
class CommonMarkMarkdownService : MarkdownService {
  private val parser = Parser.builder().build()
  private val renderer = HtmlRenderer.builder().build()

  override fun render(markdown: String): String {
    val document = parser.parse(stripBullets(markdown))

    return renderer.render(document)
  }
}
