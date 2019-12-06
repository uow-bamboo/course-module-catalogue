package uk.ac.warwick.camcat.services

import freemarker.template.Configuration
import org.springframework.stereotype.Service
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils
import uk.ac.warwick.camcat.helpers.MarkdownTemplateMethod
import uk.ac.warwick.camcat.helpers.StripBulletsTemplateMethod

interface TemplateRenderingService {
  fun render(name: String, model: Map<String, Any>): String
}

@Service
class FreeMarkerTemplateRenderingService(
  private val configuration: Configuration,
  private val markdownTemplateMethod: MarkdownTemplateMethod
) : TemplateRenderingService {
  override fun render(name: String, model: Map<String, Any>): String {
    val template = configuration.getTemplate("$name.ftlh")

    return FreeMarkerTemplateUtils.processTemplateIntoString(
      template,
      model + mapOf(
        "renderMarkdown" to markdownTemplateMethod,
        "stripBullets" to StripBulletsTemplateMethod
      )
    )
  }
}
