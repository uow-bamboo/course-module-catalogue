package uk.ac.warwick.camcat.search.queries

import uk.ac.warwick.util.termdates.AcademicYear
import java.math.BigDecimal
import java.time.LocalDate

data class ModuleQuery(
  val keywords: String? = null,
  val department: String? = null,
  val level: String? = null,
  val creditValue: BigDecimal? = null,
  val academicYear: AcademicYear = AcademicYear.starting(2020),
  val assessmentType: String? = null
)
