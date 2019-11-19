package uk.ac.warwick.camcat.sits.services

import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import uk.ac.warwick.camcat.sits.entities.Course
import uk.ac.warwick.camcat.sits.repositories.CourseRepository
import uk.ac.warwick.util.termdates.AcademicYear

interface CourseService {
  fun findByCourseCode(code: String): Course?
  fun findByModuleAcademicYear(moduleCode: String, academicYear: AcademicYear): Collection<Course>
}

@Service
class DatabaseCourseService(
  private val repo: CourseRepository,
  private val moduleService: ModuleService
) : CourseService {
  @Cacheable("courseByCode")
  override fun findByCourseCode(code: String): Course? = repo.findById(code).orElse(null)

  override fun findByModuleAcademicYear(moduleCode: String, academicYear: AcademicYear): Collection<Course> {
    return repo.findAllByModuleAndAcademicYear(moduleCode, academicYear)
  }
}

