package uk.ac.warwick.camcat.helpers

import freemarker.core.HTMLOutputFormat
import freemarker.template.SimpleScalar
import freemarker.template.TemplateMethodModelEx
import org.springframework.stereotype.Component
import uk.ac.warwick.camcat.services.MarkdownService

@Component
class MarkdownTemplateMethod(
  private val markdownService: MarkdownService
) : TemplateMethodModelEx {
  override fun exec(arguments: MutableList<Any?>?): Any {
    val markdown = arguments?.first() as SimpleScalar?

    if (markdown != null) {
      return HTMLOutputFormat.INSTANCE.fromMarkup(markdownService.render(markdown.asString))
    }

    return HTMLOutputFormat.INSTANCE.fromMarkup("")
  }
}
