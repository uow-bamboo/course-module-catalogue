package uk.ac.warwick.camcat.search.queries

import uk.ac.warwick.util.termdates.AcademicYear
import java.math.BigDecimal

data class ModuleQuery(
  val code: String? = null,
  val keywords: String? = null,
  val department: String? = null,
  val faculty: String? = null,
  val level: String? = null,
  val leader: String? = null,
  val creditValue: BigDecimal? = null,
  val academicYear: AcademicYear? = null,
  val assessmentTypes: Set<String>? = null
)
