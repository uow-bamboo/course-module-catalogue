package uk.ac.warwick.camcat.templates

import freemarker.template.Configuration
import freemarker.template.Template
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils

interface FreeMarkerRendering {
  val configuration: Configuration

  fun renderTemplate(name: String, model: Any): String {
    val template = configuration.getTemplate(name)
    return FreeMarkerTemplateUtils.processTemplateIntoString(template, model)
  }

  fun renderTemplateString(templateString: String, model: Any): String {
    val template = Template("string", templateString, configuration)
    return FreeMarkerTemplateUtils.processTemplateIntoString(template, model)
  }
}
