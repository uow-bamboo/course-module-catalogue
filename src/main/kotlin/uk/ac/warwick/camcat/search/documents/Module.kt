package uk.ac.warwick.camcat.search.documents

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType.Keyword
import org.springframework.data.elasticsearch.annotations.FieldType.Nested
import java.math.BigDecimal

@Document(indexName = "modules", type = "module")
data class Module(
  @Id val id: String,
  @Field(type = Keyword) val code: String,
  @Field(type = Keyword) val academicYear: Int,
  val title: String,
  val stemCode: String,
  val creditValue: BigDecimal,
  @Field(type = Keyword) val departmentCode: String?,
  @Field(type = Keyword) val facultyCode: String?,
  @Field(type = Keyword) val levelCode: String?,
  val text: String,
  @Field(type = Nested) val occurrences: List<ModuleOccurrence>
)

data class ModuleOccurrence(
  @Field(type = Keyword) val moduleLeader: String?,
  @Field(type = Keyword) val periodSlotCode: String,
  @Field(type = Keyword) val locationCode: String?
)
