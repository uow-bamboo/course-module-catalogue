package uk.ac.warwick.camcat.sits.services

import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import uk.ac.warwick.camcat.sits.entities.Course
import uk.ac.warwick.camcat.sits.repositories.CourseRepository

interface CourseService {
  fun findByCourseCode(code: String): Course?
}

@Service
class DatabaseCourseService(private val repo: CourseRepository) : CourseService {
  @Cacheable("courseByCode")
  override fun findByCourseCode(code: String): Course? = repo.findById(code).orElse(null)
}

