package uk.ac.warwick.camcat.sits.repositories

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.ac.warwick.camcat.sits.entities.*
import uk.ac.warwick.util.termdates.AcademicYear

@Repository
interface ModuleRepository : CrudRepository<Module, String> {
  fun findAll(pageable: Pageable): Page<Module>

  @EntityGraph(attributePaths = ["assessmentPattern.components"])
  fun findByCode(code: String): Module?

  @Query(
    """select module from Route route
    join route.pathwayDiets pathwayDiet
    join route.courses course
    join pathwayDiet.pathwayDietModules pathwayDietModule 
    join pathwayDietModule.formedModuleCollection formedModuleCollection
    join formedModuleCollection.formedModuleCollectionElements formedModuleCollectionElement
    join formedModuleCollectionElement.module module
    where pathwayDietModule.selectionStatus = :moduleSelectionStatus
    and pathwayDiet.academicYear = :academicYear 
    and pathwayDiet.block = :block
    and course.code = :courseCode
    and module.code like '%-%'
    """
  )
  fun findAllByCourseCodeAndAcademicYearAndSelectionAndBlock(
    courseCode: String,
    academicYear: AcademicYear,
    moduleSelectionStatus: ModuleSelectionStatus,
    block: String
  ): Collection<Module>

  @Query(
    """select module from ModuleRule rule
      join rule.type type
      join rule.elements body
      join body.formedModuleCollection fmc
      join fmc.formedModuleCollectionElements fme
      join fme.module module
      where rule.key.module.code = :moduleCode
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

  @Query("""select distinct new uk.ac.warwick.camcat.sits.repositories.ModuleAvailability(route.code, route.name, course.code, course.name, block.key.block, block.yearOfCourse, pathwayDietModule.selectionStatus) from Module module
    join module.formedModuleCollectionElements fme
    join fme.key.formedModuleCollection fmc
    join fmc.pathwayDietModules pathwayDietModule
    join pathwayDietModule.key.pathwayDiet pathwayDiet
    join pathwayDiet.route route
    join route.courses course
    join course.blocks block on pathwayDiet.block = block.key.block
    join block.occurrences courseBlockOccurrence
    join module.moduleOccurrences moduleOccurrence
    where module.code = :moduleCode
    and pathwayDiet.code like '%-%-%'
    and length(pathwayDiet.code) = 9
    and courseBlockOccurrence.key.academicYear = :academicYear
    and moduleOccurrence.key.academicYear = :academicYear
    and pathwayDiet.academicYear = substring(:academicYear, 1, 2)"""
    //              ^^ is a 2 digit starting year (e.g. 10), which is different to acadYear toString (e.g. 10/11)
  )
  fun findModuleAvailability(moduleCode: String, academicYear: AcademicYear): Collection<ModuleAvailability>
}

data class ModuleAvailability(
  val routeCode: String,
  val routeName: String?,
  val courseCode: String,
  val courseName: String?,
  var block: String,
  var blockYear: Int?,
  val selectionStatus: ModuleSelectionStatus?
)
