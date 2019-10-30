package uk.ac.warwick.camcat.sits.types

import org.hibernate.type.AbstractSingleColumnStandardBasicType
import org.hibernate.type.descriptor.WrapperOptions
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor
import org.hibernate.type.descriptor.java.ImmutableMutabilityPlan
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter

@Component
class DurationType :
  AbstractSingleColumnStandardBasicType<Duration>(VarcharTypeDescriptor.INSTANCE, DurationTypeDescriptor) {
  override fun getName(): String = "sits-duration"
}

object DurationTypeDescriptor : AbstractTypeDescriptor<Duration>(
  Duration::class.java,
  ImmutableMutabilityPlan.INSTANCE as ImmutableMutabilityPlan<Duration>
) {
  private val referenceDate = LocalDateTime.of(1900, Month.JANUARY, 1, 0, 0)
  private val datePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

  override fun fromString(string: String?): Duration {
    val dateTime = LocalDateTime.parse(string, datePattern)
    return Duration.between(referenceDate, dateTime)
  }

  override fun <X : Any?> unwrap(value: Duration?, type: Class<X>?, options: WrapperOptions?): X? =
    if (value != null && type == String::class) {
      referenceDate.plus(value).format(datePattern) as X
    } else null

  override fun <X : Any?> wrap(value: X, options: WrapperOptions?): Duration? =
    when (value) {
      is String -> fromString(value)
      else -> null
    }

}
