package uk.ac.warwick.camcat.system.serializers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class DurationSerializer : StdSerializer<Duration>(Duration::class.java) {
  override fun serialize(value: Duration?, gen: JsonGenerator?, provider: SerializerProvider?) {
    if (value != null) {
      gen?.writeNumber(value.toMinutes())
    } else {
      gen?.writeNull()
    }
  }
}
