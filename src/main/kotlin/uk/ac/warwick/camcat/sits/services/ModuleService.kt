package uk.ac.warwick.camcat.sits.services

import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import uk.ac.warwick.camcat.helpers.caching.Ttl
import uk.ac.warwick.camcat.helpers.caching.VariableTtlCacheHelper
import uk.ac.warwick.camcat.sits.entities.Module
import uk.ac.warwick.camcat.sits.entities.ModuleDescription
import uk.ac.warwick.camcat.sits.entities.ModuleOccurrence
import uk.ac.warwick.camcat.sits.entities.ModuleRule
import uk.ac.warwick.camcat.sits.repositories.ModuleDescriptionRepository
import uk.ac.warwick.camcat.sits.repositories.ModuleOccurrenceRepository
import uk.ac.warwick.camcat.sits.repositories.ModuleRepository
import uk.ac.warwick.camcat.sits.repositories.ModuleRuleRepository
import uk.ac.warwick.util.termdates.AcademicYear
import java.time.Duration

interface ModuleService {
  fun findAll(pageable: Pageable): Page<Module>

  fun findByModuleCode(code: String): Module?

  fun findDescriptions(moduleCode: String, academicYear: AcademicYear): Collection<ModuleDescription>

  fun findOccurrences(moduleCode: String, academicYear: AcademicYear): Collection<ModuleOccurrence>

  fun findRules(moduleCode: String, academicYear: AcademicYear): Collection<ModuleRule>
}

@Service
class DatabaseModuleService(
  private val moduleRepository: ModuleRepository,
  private val descriptionRepository: ModuleDescriptionRepository,
  private val occurrenceRepository: ModuleOccurrenceRepository,
  private val ruleRepository: ModuleRuleRepository,
  cacheManager: CacheManager
) : ModuleService {
  override fun findAll(pageable: Pageable): Page<Module> = moduleRepository.findAll(pageable)

  private val moduleCache: VariableTtlCacheHelper<Module?> = VariableTtlCacheHelper(
    cacheManager.getCache("module")!!,
    Ttl.nullableStrategy(nonNull = Duration.ofMinutes(15), isNull = Duration.ofMinutes(5)),
    Module::class.java
  )

  override fun findByModuleCode(code: String): Module? =
    moduleCache.getOrElseUpdate(code) { moduleRepository.findByCode(code) }

  @Cacheable("moduleDescriptions")
  override fun findDescriptions(moduleCode: String, academicYear: AcademicYear): Collection<ModuleDescription> =
    descriptionRepository.findAllByModuleCodeAndAcademicYear(moduleCode, academicYear)

  @Cacheable("moduleOccurrences")
  override fun findOccurrences(moduleCode: String, academicYear: AcademicYear): Collection<ModuleOccurrence> =
    occurrenceRepository.findAllByModuleCodeAndAcademicYear(moduleCode, academicYear)

  @Cacheable("moduleRules")
  override fun findRules(moduleCode: String, academicYear: AcademicYear): Collection<ModuleRule> =
    ruleRepository.findAllByModuleCodeAndAcademicYear(moduleCode, academicYear)
}

