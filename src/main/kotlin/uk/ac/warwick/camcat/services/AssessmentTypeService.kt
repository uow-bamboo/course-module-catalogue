package uk.ac.warwick.camcat.services

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.util.EntityUtils
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@Service
class AssessmentTypeService(
  private val repository: ModuleApprovalAssessmentTypeRepository
) {
  fun findByCode(code: String): AssessmentType? = repository.findAll()[code]
}

@Repository
class ModuleApprovalAssessmentTypeRepository(
  private val httpClient: CloseableHttpClient,
  private val objectMapper: ObjectMapper
) {
  @Cacheable("assessmentTypes")
  fun findAll(): Map<String, AssessmentType> {
    httpClient.execute(HttpGet("https://moduleapproval.warwick.ac.uk/api/v1/assessment_types")).use { response ->
      val string = EntityUtils.toString(response.entity)
      val assessmentTypeList = objectMapper.readValue<List<AssessmentType>>(string)

      return assessmentTypeList.groupBy { it.code }.mapValues { it.value.first() }
    }
  }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class AssessmentType(
  val code: String,
  val name: String,
  val lengthType: AssessmentLengthType
)

enum class AssessmentLengthType {
  ExamDuration, Duration, WordCount, None
}
