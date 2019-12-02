package uk.ac.warwick.camcat.services

import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import uk.ac.warwick.camcat.sits.entities.Department
import uk.ac.warwick.camcat.sits.entities.Faculty
import uk.ac.warwick.camcat.sits.repositories.DepartmentRepository

interface DepartmentService {
  fun findAllDepartments(): List<DepartmentPresenter>

  fun findAllFaculties(): List<FacultyPresenter>

  fun findByDepartmentCode(code: String): DepartmentPresenter? = findAllDepartments().find { it.code == code }

  fun findByFacultyCode(code: String): FacultyPresenter? = findAllFaculties().find { it.code == code }
}

@Service
class CombinedDepartmentService(
  private val departmentRepository: DepartmentRepository,
  private val warwickDepartmentsService: WarwickDepartmentsService
) : DepartmentService {
  @Cacheable("departments")
  override fun findAllDepartments(): List<DepartmentPresenter> {
    return departmentRepository.findAllAcademicDepartments().map { dept ->
      val warwickDepartment = warwickDepartmentsService.findByDepartmentCode(dept.code)
      DepartmentPresenter.build(
        dept,
        warwickDepartment,
        warwickDepartment?.facultyCode?.let { warwickDepartmentsService.findByFacultyCode(it) })
    }
  }

  @Cacheable("faculties")
  override fun findAllFaculties(): List<FacultyPresenter> =
    warwickDepartmentsService.findAllFaculties().map { fac -> FacultyPresenter(fac.code, fac.name )}
}

data class DepartmentPresenter(
  val code: String,
  val name: String,
  val shortName: String,
  val veryShortName: String,
  val faculty: FacultyPresenter
) {
  companion object {
    fun build(
      department: Department,
      warwickDepartment: WarwickDepartment? = null,
      faculty: WarwickFaculty? = null
    ): DepartmentPresenter =
      DepartmentPresenter(
        code = department.code,
        name = warwickDepartment?.name ?: department.name ?: department.code,
        shortName = warwickDepartment?.shortName ?: department.name ?: department.code,
        veryShortName = warwickDepartment?.veryShortName ?: department.name ?: department.code,
        faculty = FacultyPresenter.build(department.faculty, faculty)
      )
  }
}

data class FacultyPresenter(val code: String?, val name: String?) {
  companion object {
    fun build(faculty: Faculty?, warwickFaculty: WarwickFaculty?): FacultyPresenter {
      val name = warwickFaculty?.name ?: faculty?.name
      return FacultyPresenter(
        code = warwickFaculty?.code ?: faculty?.code,
        name = name
      )
    }
  }
}

