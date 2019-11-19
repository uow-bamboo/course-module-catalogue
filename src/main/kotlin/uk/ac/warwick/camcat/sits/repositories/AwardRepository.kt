package uk.ac.warwick.camcat.sits.repositories

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.ac.warwick.camcat.sits.entities.Award
import uk.ac.warwick.camcat.sits.entities.Course

@Repository
interface AwardRepository : CrudRepository<Award, String> {
  fun findAll(pageable: Pageable): Page<Award>

  @Query("from Award where inUse = true and code = :code")
  fun findByCode(code: String): Award?
}
