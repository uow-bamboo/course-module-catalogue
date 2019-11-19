package uk.ac.warwick.camcat.sits.repositories

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.ac.warwick.camcat.sits.entities.*
import uk.ac.warwick.util.termdates.AcademicYear

@Repository
interface PathwayDietModuleRepository : CrudRepository<PathwayDietModule, PathwayDietModuleKey> {
  @Query("from PathwayDietModule where key.pathwayDiet in :pathwayDiets and selectionStatus = :selectionStatus")
  fun PathwayDietModuleByPathwayDietsAndSelectionCode(
    pathwayDiets: Collection<PathwayDiet>,
    selectionStatus: String
  ): Collection<PathwayDietModule>
}
