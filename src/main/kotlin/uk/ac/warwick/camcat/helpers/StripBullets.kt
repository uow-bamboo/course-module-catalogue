package uk.ac.warwick.camcat.helpers

import freemarker.template.SimpleScalar
import freemarker.template.TemplateMethodModelEx
import uk.ac.warwick.camcat.helpers.StripBullets.stripBullets

object StripBullets {
  fun stripBullets(string: String) = string.replace("·", "")
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
