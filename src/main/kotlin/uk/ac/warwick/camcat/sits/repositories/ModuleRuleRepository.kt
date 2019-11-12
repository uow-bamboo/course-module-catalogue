package uk.ac.warwick.camcat.sits.repositories

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.ac.warwick.camcat.sits.entities.ModuleRule
import uk.ac.warwick.camcat.sits.entities.ModuleRuleKey
import uk.ac.warwick.util.termdates.AcademicYear

@Repository
interface ModuleRuleRepository : CrudRepository<ModuleRule, ModuleRuleKey> {
  @Query("from ModuleRule where key.moduleCode = :moduleCode and academicYear = :academicYear")
  fun findAllByModuleCodeAndAcademicYear(moduleCode: String, academicYear: AcademicYear): Collection<ModuleRule>
}
