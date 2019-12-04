package uk.ac.warwick.camcat.services

import com.vladsch.flexmark.ext.autolink.AutolinkExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import org.springframework.stereotype.Service
import uk.ac.warwick.camcat.helpers.StripBullets.replaceBullets

interface MarkdownService {
  fun render(markdown: String): String
}

@Service
class CommonMarkMarkdownService(private val sanitiseHtmlService: SanitiseHtmlService) : MarkdownService {
  private val parser = Parser.builder()
    .extensions(mutableListOf(AutolinkExtension.create()))
    .build()

  private val renderer = HtmlRenderer.builder()
    .softBreak("<br>")
    .build()

  override fun render(markdown: String): String = sanitiseHtmlService.sanitise(renderUnsafe(markdown))

  private fun renderUnsafe(markdown: String): String =
    renderer.render(parser.parse(replaceBullets(markdown, with = "-")))
}
