package uk.ac.warwick.camcat.sits.repositories

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import uk.ac.warwick.camcat.sits.entities.Faculty

interface FacultyRepository : CrudRepository<Faculty, String> {
  @Query("from Faculty where inUse = true and not (code = 'X') order by name")
  override fun findAll(): Iterable<Faculty>
}
