package uk.ac.warwick.camcat.services

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import uk.ac.warwick.camcat.sits.repositories.CourseRepository
import uk.ac.warwick.camcat.sits.repositories.ModuleRepository
import uk.ac.warwick.camcat.system.cache.VariableTtlCacheDecorator

interface DepartmentService {
  /**
   * Find all departments that contain courses or modules.
   */
  fun findAllDepartments(): List<Department>

  /**
   * Find all faculties that contain courses or modules.
   */
  fun findAllFaculties(): List<Faculty>

  fun findByDepartmentCode(code: String): Department?

  fun findByFacultyCode(code: String): Faculty?
}

@Service
class CourseModuleDepartmentService(
  private val warwickDepartmentsService: WarwickDepartmentsService,
  private val moduleRepository: ModuleRepository,
  private val courseRepository: CourseRepository,
  @Value("#{variableTtlCacheFactory.getCache('departments')}") private val cache: VariableTtlCacheDecorator
) : DepartmentService {
  override fun findAllDepartments(): List<Department> = cache.get("allDepartments") {
    val departmentCodes =
      moduleRepository.findDistinctDepartmentCodes() + courseRepository.findDistinctDepartmentCodes()

    warwickDepartmentsService.findAllDepartments()
      .filter { dept -> departmentCodes.contains(dept.code) }
      .map { dept ->
        Department.build(dept, warwickDepartmentsService.findByFacultyCode(dept.facultyCode)!!)
      }
      .sortedBy { it.name }
  }.orEmpty()

  override fun findAllFaculties(): List<Faculty> = cache.get("allFaculties") {
    findAllDepartments().map { it.faculty }.distinct().sortedBy { it.name }
  }.orEmpty()

  override fun findByDepartmentCode(code: String): Department? = cache.get("departmentByCode($code)") {
    warwickDepartmentsService.findByDepartmentCode(code)?.let { dept ->
      Department.build(dept, warwickDepartmentsService.findByFacultyCode(dept.facultyCode)!!)
    }
  }

  override fun findByFacultyCode(code: String): Faculty? = cache.get("facultyByCode($code)") {
    warwickDepartmentsService.findByFacultyCode(code)?.let { fac ->
      Faculty(fac.code, fac.name)
    }
  }
}

data class Department(
  val code: String,
  val name: String,
  val shortName: String,
  val veryShortName: String,
  val faculty: Faculty
) {
  companion object {
    fun build(
      department: WarwickDepartment,
      faculty: WarwickFaculty
    ): Department =
      Department(
        code = department.code,
        name = department.name,
        shortName = department.shortName,
        veryShortName = department.veryShortName,
        faculty = Faculty(
          code = faculty.code,
          name = faculty.name
        )
      )
  }
}

data class Faculty(val code: String, val name: String)
