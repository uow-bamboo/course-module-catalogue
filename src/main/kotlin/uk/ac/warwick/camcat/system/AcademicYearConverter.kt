package uk.ac.warwick.camcat.system

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import uk.ac.warwick.util.termdates.AcademicYear

@Component
class AcademicYearConverter : Converter<String, AcademicYear> {
  override fun convert(source: String): AcademicYear? = try {
    AcademicYear.starting(source.toInt())
  } catch (e: NumberFormatException) {
    throw IllegalArgumentException(e)
  }
}
