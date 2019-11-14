package uk.ac.warwick.camcat.sits.services

import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import uk.ac.warwick.camcat.sits.entities.*
import uk.ac.warwick.camcat.sits.repositories.*
import uk.ac.warwick.util.termdates.AcademicYear

interface DepartmentService {
  fun findAllAcademic(): Iterable<Department>
}

@Service
class DatabaseDepartmentService(
  private val departmentRepository: DepartmentRepository
) : DepartmentService {
  @Cacheable("academicDepartments")
  override fun findAllAcademic(): Iterable<Department> = departmentRepository.findAllAcademicDepartments()
}

