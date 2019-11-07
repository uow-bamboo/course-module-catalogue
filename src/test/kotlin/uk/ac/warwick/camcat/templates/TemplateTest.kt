package uk.ac.warwick.camcat.templates

import freemarker.template.Configuration
import freemarker.template.Template
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils
import uk.ac.warwick.camcat.context.ContextTest

abstract class TemplateTest : ContextTest() {
  @Autowired
  lateinit var configuration: Configuration

  fun renderTemplate(name: String, model: Any): String {
    val template = configuration.getTemplate(name)
    return FreeMarkerTemplateUtils.processTemplateIntoString(template, model)
  }

  fun renderTemplateString(templateString: String, model: Any): String {
    val template = Template("string", templateString, configuration)
    return FreeMarkerTemplateUtils.processTemplateIntoString(template, model)
  }
}
