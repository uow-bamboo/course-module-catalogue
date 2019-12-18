package uk.ac.warwick.camcat.services

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.util.EntityUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import uk.ac.warwick.camcat.system.cache.VariableTtlCacheDecorator

@Service
class ModuleApprovalAssessmentTypeService(
  private val repository: ModuleApprovalAssessmentTypeRepository
) {
  fun findByCode(code: String): AssessmentType? = repository.findAll()[code]
}

@Repository
class ModuleApprovalAssessmentTypeRepository(
  private val httpClient: CloseableHttpClient,
  private val objectMapper: ObjectMapper,
  @Value("#{variableTtlCacheFactory.getCache('moduleApprovalAssessmentTypes')}") private val cache: VariableTtlCacheDecorator
) {
  fun findAll(): Map<String, AssessmentType> = cache.get("all") {
    httpClient.execute(HttpGet("https://moduleapproval.warwick.ac.uk/api/v1/assessment_types")).use { response ->
      val string = EntityUtils.toString(response.entity)
      val assessmentTypeList = objectMapper.readValue<List<AssessmentType>>(string)

      assessmentTypeList.groupBy { it.code }.mapValues { it.value.first() }
    }
  }.orEmpty()
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
