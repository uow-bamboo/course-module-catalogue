package uk.ac.warwick.camcat.sits.services

import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import uk.ac.warwick.camcat.sits.entities.*
import uk.ac.warwick.camcat.sits.repositories.ModuleDescriptionRepository
import uk.ac.warwick.camcat.sits.repositories.ModuleOccurrenceRepository
import uk.ac.warwick.camcat.sits.repositories.ModuleRepository
import uk.ac.warwick.camcat.sits.repositories.TopicRepository
import uk.ac.warwick.util.termdates.AcademicYear

interface ModuleService {
  fun findAll(pageable: Pageable): Page<Module>

  fun findByModuleCode(code: String): Module?

  fun findDescriptions(moduleCode: String, academicYear: AcademicYear): Collection<ModuleDescription>

  fun findOccurrences(moduleCode: String, academicYear: AcademicYear): Collection<ModuleOccurrence>

  fun findTopics(moduleCode: String, academicYear: AcademicYear): Collection<Topic>

  fun findRelatedModules(moduleCode: String, academicYear: AcademicYear): RelatedModules
}

@Service
class DatabaseModuleService(
  private val moduleRepository: ModuleRepository,
  private val descriptionRepository: ModuleDescriptionRepository,
  private val occurrenceRepository: ModuleOccurrenceRepository,
  private val topicRepository: TopicRepository
) : ModuleService {
  override fun findAll(pageable: Pageable): Page<Module> = moduleRepository.findAll(pageable)

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
      postRequisites = moduleRepository.findModulesWithRulesContainingModule(moduleCode, RuleType.PreRequisite, academicYear),
      antiRequisites = moduleRepository.findModulesInRuleForModule(moduleCode, RuleType.AntiRequisite, academicYear)
    )
}

data class RelatedModules(
  val preRequisites: Collection<Module>,
  val postRequisites: Collection<Module>,
  val antiRequisites: Collection<Module>
) {
  val empty: Boolean
    get() = preRequisites.isEmpty() && postRequisites.isEmpty() && antiRequisites.isEmpty()
}
