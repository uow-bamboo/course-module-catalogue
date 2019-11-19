package uk.ac.warwick.camcat.sits.repositories

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.ac.warwick.camcat.sits.entities.Award
import uk.ac.warwick.camcat.sits.entities.Course
import uk.ac.warwick.camcat.sits.entities.Programme
import uk.ac.warwick.camcat.sits.entities.Route

@Repository
interface ProgrammeRepository : CrudRepository<Programme, String> {
  fun findAll(pageable: Pageable): Page<Programme>

  @Query("from Programme where inUse = true and code = :code")
  fun findByCode(code: String): Programme?
}
