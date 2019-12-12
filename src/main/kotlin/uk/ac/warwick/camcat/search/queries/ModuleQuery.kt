package uk.ac.warwick.camcat.search.queries

import uk.ac.warwick.util.termdates.AcademicYear
import java.math.BigDecimal

data class ModuleQuery(
  val keywords: String? = null,
  val departments: List<String> = emptyList(),
  val levels: List<String> = emptyList(),
  val creditValues: List<BigDecimal> = emptyList(),
  val academicYear: AcademicYear = AcademicYear.starting(2020),
  val assessmentTypes: List<String> = emptyList()
)
