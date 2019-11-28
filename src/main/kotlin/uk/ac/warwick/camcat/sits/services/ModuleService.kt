package uk.ac.warwick.camcat.sits.services

import org.springframework.cache.annotation.Cacheable
import org.springframework.data.util.Streamable
import org.springframework.stereotype.Service
import uk.ac.warwick.camcat.sits.entities.*
import uk.ac.warwick.camcat.sits.repositories.*
import uk.ac.warwick.util.termdates.AcademicYear

interface ModuleService {
  fun findByModuleCode(code: String): Module?

  fun findAllWithOccurrenceInAcademicYear(academicYear: AcademicYear): Streamable<Module>

  fun findDescriptions(moduleCode: String, academicYear: AcademicYear): Collection<ModuleDescription>

  fun findOccurrences(moduleCode: String, academicYear: AcademicYear): Collection<ModuleOccurrence>

  fun findTopics(moduleCode: String, academicYear: AcademicYear): Collection<Topic>

  fun findRelatedModules(moduleCode: String, academicYear: AcademicYear): RelatedModules

  fun findRules(moduleCode: String, academicYear: AcademicYear): Collection<ModuleRule>

  fun findModulesByCourseYearSelectionBlock(
    courseCode: String,
    academicYear: AcademicYear,
    moduleSelectionStatus: ModuleSelectionStatus,
    block: String
  ): Collection<Module>

  fun findAvailability(moduleCode: String, academicYear: AcademicYear): Collection<ModuleAvailability>
}

@Service
class DatabaseModuleService(
  private val moduleRepository: ModuleRepository,
  private val descriptionRepository: ModuleDescriptionRepository,
  private val occurrenceRepository: ModuleOccurrenceRepository,
  private val ruleRepository: ModuleRuleRepository,
  private val topicRepository: TopicRepository
) : ModuleService {
  override fun findAllWithOccurrenceInAcademicYear(academicYear: AcademicYear): Streamable<Module> =
    moduleRepository.findAllWithOccurrenceInAcademicYear(academicYear)

  @Cacheable("module")
  override fun findByModuleCode(code: String): Module? = moduleRepository.findByCode(code)

  @Cacheable("moduleDescriptions")
  override fun findDescriptions(moduleCode: String, academicYear: AcademicYear): Collection<ModuleDescription> =
    descriptionRepository.findAllByModuleCodeAndAcademicYear(moduleCode, academicYear)

  @Cacheable("moduleOccurrences")
  override fun findOccurrences(moduleCode: String, academicYear: AcademicYear): Collection<ModuleOccurrence> =
    occurrenceRepository.findAllByModuleCodeAndAcademicYear(moduleCode, academicYear)

  @Cacheable("moduleTopics")
  override fun findTopics(moduleCode: String, academicYear: AcademicYear): Collection<Topic> =
    topicRepository.findByModuleCodeAndAcademicYear(moduleCode, academicYear)

  @Cacheable("relatedModules")
  override fun findRelatedModules(moduleCode: String, academicYear: AcademicYear): RelatedModules =
    RelatedModules(
      preRequisites = moduleRepository.findModulesInRuleForModule(moduleCode, RuleType.PreRequisite, academicYear),
      postRequisites = moduleRepository.findModulesWithRulesContainingModule(
        moduleCode,
        RuleType.PreRequisite,
        academicYear
      ),
      antiRequisites = moduleRepository.findModulesInRuleForModule(moduleCode, RuleType.AntiRequisite, academicYear)
    )

  @Cacheable("moduleRules")
  override fun findRules(moduleCode: String, academicYear: AcademicYear): Collection<ModuleRule> =
    ruleRepository.findAllByModuleCodeAndAcademicYear(moduleCode, academicYear)

  override fun findModulesByCourseYearSelectionBlock(
    courseCode: String,
    academicYear: AcademicYear,
    moduleSelectionStatus: ModuleSelectionStatus,
    block: String
  ): Collection<Module> {
    return moduleRepository.findAllByCourseCodeAndAcademicYearAndSelectionAndBlock(
      courseCode = courseCode,
      academicYear = academicYear,
      moduleSelectionStatus = moduleSelectionStatus,
      block = block
    )
  }

  @Cacheable("moduleAvailability")
  override fun findAvailability(moduleCode: String, academicYear: AcademicYear): Collection<ModuleAvailability> =
    moduleRepository.findModuleAvailability(moduleCode, academicYear)
}

data class RelatedModules(
  val preRequisites: Collection<Module>,
  val postRequisites: Collection<Module>,
  val antiRequisites: Collection<Module>
) {
  val empty: Boolean
    get() = preRequisites.isEmpty() && postRequisites.isEmpty() && antiRequisites.isEmpty()
}
