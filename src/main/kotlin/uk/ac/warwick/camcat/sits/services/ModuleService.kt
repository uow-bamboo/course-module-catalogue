package uk.ac.warwick.camcat.sits.services

import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import uk.ac.warwick.camcat.sits.entities.Module
import uk.ac.warwick.camcat.sits.entities.ModuleDescription
import uk.ac.warwick.camcat.sits.entities.ModuleOccurrence
import uk.ac.warwick.camcat.sits.repositories.ModuleDescriptionRepository
import uk.ac.warwick.camcat.sits.repositories.ModuleOccurrenceRepository
import uk.ac.warwick.camcat.sits.repositories.ModuleRepository
import uk.ac.warwick.util.termdates.AcademicYear

interface ModuleService {
  fun findByModuleCode(code: String): Module?

  fun findDescriptions(moduleCode: String, academicYear: AcademicYear): Collection<ModuleDescription>

  fun findOccurrences(moduleCode: String, academicYear: AcademicYear): Collection<ModuleOccurrence>
}

@Service
class DatabaseModuleService(
  private val moduleRepository: ModuleRepository,
  private val descriptionRepository: ModuleDescriptionRepository,
  private val occurrenceRepository: ModuleOccurrenceRepository
) : ModuleService {
  @Cacheable("module")
  override fun findByModuleCode(code: String): Module? = moduleRepository.findByCode(code)

  @Cacheable("moduleDescriptions")
  override fun findDescriptions(moduleCode: String, academicYear: AcademicYear): Collection<ModuleDescription> =
    descriptionRepository.findAllByModuleCodeAndAcademicYear(moduleCode, academicYear)

  @Cacheable("moduleOccurrences")
  override fun findOccurrences(moduleCode: String, academicYear: AcademicYear): Collection<ModuleOccurrence> =
    occurrenceRepository.findAllByModuleCodeAndAcademicYear(moduleCode, academicYear)
}

