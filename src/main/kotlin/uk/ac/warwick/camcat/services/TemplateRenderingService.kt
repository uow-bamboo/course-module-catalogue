package uk.ac.warwick.camcat.services

import freemarker.template.Configuration
import org.springframework.stereotype.Service
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils

interface TemplateRenderingService {
  fun render(name: String, model: Any): String
}

@Service
class FreeMarkerTemplateRenderingService(
  private val configuration: Configuration
) : TemplateRenderingService {
  override fun render(name: String, model: Any): String {
    val template = configuration.getTemplate("$name.ftlh")
    return FreeMarkerTemplateUtils.processTemplateIntoString(template, model)
  }
}
