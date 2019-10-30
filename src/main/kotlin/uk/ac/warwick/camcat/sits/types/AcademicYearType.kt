package uk.ac.warwick.camcat.sits.types

import org.hibernate.type.AbstractSingleColumnStandardBasicType
import org.hibernate.type.descriptor.WrapperOptions
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor
import org.hibernate.type.descriptor.java.ImmutableMutabilityPlan
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor
import org.springframework.stereotype.Component
import uk.ac.warwick.util.termdates.AcademicYear

@Component
class AcademicYearType :
  AbstractSingleColumnStandardBasicType<AcademicYear>(VarcharTypeDescriptor.INSTANCE, AcademicYearTypeDescriptor) {
  override fun getName(): String = "sits-academic-year"
}

object AcademicYearTypeDescriptor : AbstractTypeDescriptor<AcademicYear>(
  AcademicYear::class.java,
  ImmutableMutabilityPlan.INSTANCE as ImmutableMutabilityPlan<AcademicYear>
) {
  override fun fromString(string: String?): AcademicYear = AcademicYear.parse(string)

  override fun <X : Any?> unwrap(value: AcademicYear?, type: Class<X>?, options: WrapperOptions?): X? =
    value?.toString() as X?

  override fun <X : Any?> wrap(value: X, options: WrapperOptions?): AcademicYear? = when (value) {
    is String -> fromString(value)
    else -> null
  }

}
