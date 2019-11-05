package uk.ac.warwick.camcat.search.documents

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import java.math.BigDecimal

@Document(indexName = "modules", type = "module")
data class Module(
  @Id val id: String,
  val code: String,
  val academicYear: Int,
  val title: String,
  val stemCode: String,
  val creditValue: BigDecimal,
  val departmentCode: String?,
  val facultyCode: String?,
  val levelCode: String?,
  val text: String,
  val occurrences: List<ModuleOccurrence>
)

data class ModuleOccurrence(
  val moduleLeader: String?,
  val periodSlotCode: String,
  val locationCode: String?
)
