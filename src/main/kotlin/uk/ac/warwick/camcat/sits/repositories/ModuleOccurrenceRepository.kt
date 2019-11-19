package uk.ac.warwick.camcat.sits.repositories

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.ac.warwick.camcat.sits.entities.ModuleOccurrence
import uk.ac.warwick.camcat.sits.entities.ModuleOccurrenceKey
import uk.ac.warwick.util.termdates.AcademicYear

@Repository
interface ModuleOccurrenceRepository : CrudRepository<ModuleOccurrence, ModuleOccurrenceKey> {
  @Query("from ModuleOccurrence where key.module.code = :moduleCode and key.academicYear = :academicYear")
  fun findAllByModuleCodeAndAcademicYear(moduleCode: String, academicYear: AcademicYear): Collection<ModuleOccurrence>
}
