package uk.ac.warwick.camcat.sits.repositories

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import uk.ac.warwick.camcat.sits.entities.Level

interface LevelRepository : CrudRepository<Level, String> {
  @Query("from Level where inUse = true order by name")
  override fun findAll(): Iterable<Level>
}
