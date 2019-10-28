package uk.ac.warwick.camcat.sits.repositories

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.ac.warwick.camcat.sits.entities.ModuleDescription
import uk.ac.warwick.camcat.sits.entities.ModuleDescriptionKey
import uk.ac.warwick.util.termdates.AcademicYear

@Repository
interface ModuleDescriptionRepository : CrudRepository<ModuleDescription, ModuleDescriptionKey> {
  @Query("from ModuleDescription where key.moduleCode = :moduleCode and academicYear = :academicYear")
  fun findAllByModuleCodeAndAcademicYear(moduleCode: String, academicYear: AcademicYear): Collection<ModuleDescription>
}
