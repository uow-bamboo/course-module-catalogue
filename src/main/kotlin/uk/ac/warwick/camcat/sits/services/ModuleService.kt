package uk.ac.warwick.camcat.sits.services

import org.springframework.data.util.Streamable
import org.springframework.stereotype.Service
import uk.ac.warwick.camcat.sits.entities.*
import uk.ac.warwick.camcat.sits.repositories.*
import uk.ac.warwick.util.termdates.AcademicYear

interface ModuleService {
  fun findByModuleCode(code: String): Module?

  fun findAllCodesByStemCode(stemCode: String): Collection<String>

  fun findAllWithOccurrenceInAcademicYear(academicYear: AcademicYear): Streamable<Module>

  fun findAssessmentComponents(moduleCode: String, academicYear: AcademicYear): Collection<AssessmentComponent>

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
  private val topicRepository: TopicRepository,
  private val assessmentComponentRepository: AssessmentComponentRepository
) : ModuleService {
  override fun findAllWithOccurrenceInAcademicYear(academicYear: AcademicYear): Streamable<Module> =
    moduleRepository.findAllWithOccurrenceInAcademicYear(academicYear)

  override fun findByModuleCode(code: String): Module? = moduleRepository.findByCode(code)

  override fun findAllCodesByStemCode(stemCode: String): Collection<String> =
    moduleRepository.findAllCodesLike("$stemCode-%")

  override fun findDescriptions(moduleCode: String, academicYear: AcademicYear): Collection<ModuleDescription> =
    descriptionRepository.findAllByModuleCodeAndAcademicYear(moduleCode, academicYear)

  override fun findOccurrences(moduleCode: String, academicYear: AcademicYear): Collection<ModuleOccurrence> =
    occurrenceRepository.findAllByModuleCodeAndAcademicYear(moduleCode, academicYear)

  override fun findTopics(moduleCode: String, academicYear: AcademicYear): Collection<Topic> =
    topicRepository.findByModuleCodeAndAcademicYear(moduleCode, academicYear)

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

  override fun findAvailability(moduleCode: String, academicYear: AcademicYear): Collection<ModuleAvailability> =
    moduleRepository.findModuleAvailability(moduleCode, academicYear)

  override fun findAssessmentComponents(
    moduleCode: String,
    academicYear: AcademicYear
  ): Collection<AssessmentComponent> =
    assessmentComponentRepository.findAllByAssessmentPatternCodeAndAcademicYear(moduleCode, academicYear)

}

data class RelatedModules(
  val preRequisites: Collection<Module>,
  val postRequisites: Collection<Module>,
  val antiRequisites: Collection<Module>
) {
  val empty: Boolean
    get() = preRequisites.isEmpty() && postRequisites.isEmpty() && antiRequisites.isEmpty()
}
