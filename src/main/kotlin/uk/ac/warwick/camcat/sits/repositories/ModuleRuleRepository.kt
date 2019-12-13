package uk.ac.warwick.camcat.sits.repositories

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.ac.warwick.camcat.sits.entities.ModuleModuleRule
import uk.ac.warwick.camcat.sits.entities.ModuleModuleRuleKey
import uk.ac.warwick.util.termdates.AcademicYear

@Repository
interface ModuleRuleRepository : CrudRepository<ModuleModuleRule, ModuleModuleRuleKey> {
  @Query("from ModuleModuleRule where key.module.code = :moduleCode and academicYear = :academicYear")
  fun findAllByModuleCodeAndAcademicYear(moduleCode: String, academicYear: AcademicYear): Collection<ModuleModuleRule>
}
