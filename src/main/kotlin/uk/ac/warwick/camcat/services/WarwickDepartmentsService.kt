package uk.ac.warwick.camcat.services

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.util.EntityUtils
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.io.Serializable

@Service
class WarwickDepartmentsService(
  private val repository: WarwickDepartmentsRepository
) {
  fun findAll(): Collection<Department> = repository.findAll().values

  fun findByDepartmentCode(code: String): Department? = repository.findAll()[code]
}

@Component
class WarwickDepartmentsRepository(
  private val httpClient: CloseableHttpClient,
  private val objectMapper: ObjectMapper
) {
  @Cacheable("departments")
  fun findAll(): Map<String, Department> {
    httpClient.execute(HttpGet("https://departments.warwick.ac.uk/public/api/department.json")).use { response ->
      val string = EntityUtils.toString(response.entity)
      val departmentList = objectMapper.readValue<List<Department>>(string)

      return departmentList.groupBy { it.code }.mapValues { it.value.first() }
    }
  }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class Department(
  val inUse: Boolean,
  val code: String,
  val name: String,
  val shortName: String,
  val veryShortName: String
) : Serializable
