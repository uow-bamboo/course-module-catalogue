package uk.ac.warwick.camcat.sits.repositories

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.ac.warwick.camcat.sits.entities.Module
import uk.ac.warwick.util.termdates.AcademicYear

@Repository
interface ModuleRepository : CrudRepository<Module, String> {
  fun findAll(pageable: Pageable): Page<Module>

  fun findByCode(code: String): Module?

  @Query(
    """select module from ModuleRule rule
      join rule.type type
      join rule.elements body
      join body.formedModuleCollection fmc
      join fmc.formedModuleCollectionElements fme
      join fme.module module
      where rule.key.moduleCode = :moduleCode
      and rule.academicYear = :academicYear
      and type.code = :ruleType"""
  )
  fun findModulesInRuleForModule(moduleCode: String, ruleType: String, academicYear: AcademicYear): Collection<Module>

  @Query(
    """select ruleModule from Module module
      join module.formedModuleCollectionElements fme
      join fme.key.formedModuleCollection fmc
      join fmc.moduleRuleElements body
      join body.rule rule
      join rule.module ruleModule
      join rule.type type
      where module.code = :moduleCode
      and rule.academicYear = :academicYear
      and type.code = :ruleType"""
  )
  fun findModulesWithRulesContainingModule(
    moduleCode: String,
    ruleType: String,
    academicYear: AcademicYear
  ): Collection<Module>
}
