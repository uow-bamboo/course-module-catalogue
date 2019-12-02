package uk.ac.warwick.camcat.services

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.util.EntityUtils
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Service
class WarwickDepartmentsService(
  private val repository: WarwickDepartmentsRepository
) {
  fun findAllDepartments(): Collection<WarwickDepartment> = repository.findAllDepartments().values

  fun findAllFaculties(): Collection<WarwickFaculty> = repository.findAllFaculties().values

  fun findByDepartmentCode(code: String): WarwickDepartment? = repository.findAllDepartments()[code]

  fun findByFacultyCode(code: String): WarwickFaculty? = repository.findAllFaculties()[code]
}

@Component
class WarwickDepartmentsRepository(
  private val httpClient: CloseableHttpClient,
  private val objectMapper: ObjectMapper
) {
  @Cacheable("warwickDepartments")
  fun findAllDepartments(): Map<String, WarwickDepartment> {
    httpClient.execute(HttpGet("https://departments.warwick.ac.uk/public/api/department.json")).use { response ->
      val string = EntityUtils.toString(response.entity)
      val departmentList = objectMapper.readValue<List<WarwickDepartment>>(string)

      return departmentList.groupBy { it.code }.mapValues { it.value.first() }
    }
  }

  @Cacheable("warwickFaculties")
  fun findAllFaculties(): Map<String, WarwickFaculty> {
    httpClient.execute(HttpGet("https://departments.warwick.ac.uk/public/api/faculty.json")).use { response ->
      val string = EntityUtils.toString(response.entity)
      val facultyList = objectMapper.readValue<List<WarwickFaculty>>(string)

      return facultyList.groupBy { it.code }.mapValues { it.value.first() }
    }
  }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class WarwickDepartment(
  val inUse: Boolean,
  val code: String,
  val name: String,
  val shortName: String,
  val veryShortName: String,
  @JsonProperty("faculty")
  val facultyCode: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class WarwickFaculty(
  val code: String,
  val name: String
)
