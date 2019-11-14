package uk.ac.warwick.camcat.sits.services

import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import uk.ac.warwick.camcat.sits.entities.Faculty
import uk.ac.warwick.camcat.sits.repositories.FacultyRepository

interface FacultyService {
  fun findAll(): Iterable<Faculty>
}

@Service
class DatabaseFacultyService(
  private val facultyRepository: FacultyRepository
) : FacultyService {
  @Cacheable("faculties")
  override fun findAll(): Iterable<Faculty> = facultyRepository.findAll()
}

