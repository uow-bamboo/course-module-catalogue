package uk.ac.warwick.camcat.system.context

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import uk.ac.warwick.util.termdates.AcademicYear
import java.time.Duration

@Configuration
class JsonContext {
  @Bean
  fun objectMapper(): ObjectMapper = ObjectMapper()
    .registerModule(KotlinModule())
    .registerModule(
      SimpleModule()
        .addSerializer(AcademicYear::class.java, ToStringSerializer())
        .addSerializer(Duration::class.java, ToStringSerializer())
    )
}
