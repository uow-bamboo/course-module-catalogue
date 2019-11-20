package uk.ac.warwick.camcat.helpers

import freemarker.template.SimpleScalar
import freemarker.template.TemplateMethodModelEx
import uk.ac.warwick.camcat.helpers.StripBullets.stripBullets

object StripBullets {
  private val pattern = Regex("^\\s*[•·]\\s*", RegexOption.MULTILINE)

  fun stripBullets(string: String) = string.replace(pattern, "")

  fun replaceBullets(string: String, with: String) = string.replace(pattern, "$with ")
}

object StripBulletsTemplateMethod : TemplateMethodModelEx {
  override fun exec(arguments: MutableList<Any?>?): Any {
    val string = arguments?.first() as SimpleScalar?

    if (string != null) {
      return stripBullets(string.asString)
    }

    return ""
  }
}
