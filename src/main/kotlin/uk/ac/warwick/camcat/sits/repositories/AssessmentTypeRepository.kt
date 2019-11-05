package uk.ac.warwick.camcat.sits.repositories

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import uk.ac.warwick.camcat.sits.entities.AssessmentType

interface AssessmentTypeRepository : CrudRepository<AssessmentType, String> {
  @Query("from AssessmentType where printName = true and inUse = true order by name")
  override fun findAll(): Iterable<AssessmentType>
}
