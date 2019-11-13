package uk.ac.warwick.camcat.search.queries

import uk.ac.warwick.util.termdates.AcademicYear
import java.math.BigDecimal

data class ModuleQuery(
  val code: String?,
  val keywords: String?,
  val department: String?,
  val faculty: String?,
  val level: String?,
  val leader: String?,
  val creditValue: BigDecimal?,
  val academicYear: AcademicYear?,
  val assessmentTypes: Set<String>?
)
