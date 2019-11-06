package uk.ac.warwick.camcat.sits.repositories

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.ac.warwick.camcat.sits.entities.Course

@Repository
interface CourseRepository : CrudRepository<Course, String> {
  fun findAll(pageable: Pageable): Page<Course>

  fun findByCode(code: String): Course?
}
