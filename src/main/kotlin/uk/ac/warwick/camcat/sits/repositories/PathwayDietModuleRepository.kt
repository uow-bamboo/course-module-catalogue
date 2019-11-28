package uk.ac.warwick.camcat.sits.repositories

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.ac.warwick.camcat.sits.entities.ModuleSelectionStatus
import uk.ac.warwick.camcat.sits.entities.PathwayDiet
import uk.ac.warwick.camcat.sits.entities.PathwayDietModule
import uk.ac.warwick.camcat.sits.entities.PathwayDietModuleKey

@Repository
interface PathwayDietModuleRepository : CrudRepository<PathwayDietModule, PathwayDietModuleKey> {
  @Query("from PathwayDietModule where key.pathwayDiet in :pathwayDiets and selectionStatus = :selectionStatus")
  fun PathwayDietModuleByPathwayDietsAndSelectionStatus(
    pathwayDiets: Collection<PathwayDiet>,
    selectionStatus: ModuleSelectionStatus
  ): Collection<PathwayDietModule>
}
