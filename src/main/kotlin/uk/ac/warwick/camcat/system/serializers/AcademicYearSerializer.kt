package uk.ac.warwick.camcat.system.serializers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.springframework.stereotype.Component
import uk.ac.warwick.util.termdates.AcademicYear

@Component
class AcademicYearSerializer : StdSerializer<AcademicYear>(AcademicYear::class.java) {
  override fun serialize(value: AcademicYear?, gen: JsonGenerator?, provider: SerializerProvider?) {
    gen?.writeString(value.toString())
  }
}
