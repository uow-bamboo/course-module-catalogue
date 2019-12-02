package uk.ac.warwick.camcat.search.documents

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType.*
import org.springframework.data.elasticsearch.annotations.Score
import java.math.BigDecimal

@Document(indexName = "modules", type = "module")
data class Module(
  @Id val id: String,
  @Field(type = Keyword) val code: String,
  @Field(type = Keyword) val academicYear: Int,
  @Field(type = Text, analyzer = "english") val title: String,
  @Field(type = Keyword) val stemCode: String,
  @Field(type = Keyword) val creditValue: BigDecimal,
  @Field(type = Keyword) val departmentCode: String?,
  @Field(type = Keyword) val departmentShortName: String?,
  @Field(type = Text, analyzer = "english") val departmentName: String?,
  @Field(type = Keyword) val facultyCode: String?,
  @Field(type = Text, analyzer = "english") val facultyName: String?,
  @Field(type = Keyword) val levelCode: String?,
  @Field(type = Keyword) val leader: String?,
  @Field(type = Text) val leaderName: String?,
  @Field(type = Text, analyzer = "english") val text: String,
  @Field(type = Nested) val assessmentComponents: List<AssessmentComponent>,
  @Score val score: Float? = null
)

data class AssessmentComponent(
  @Field(type = Keyword) val typeCode: String,
  @Field(type = Text) val name: String?
)

