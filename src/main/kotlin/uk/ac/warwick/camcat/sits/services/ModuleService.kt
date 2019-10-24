package uk.ac.warwick.camcat.sits.services

import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import uk.ac.warwick.camcat.sits.entities.Module
import uk.ac.warwick.camcat.sits.entities.ModuleDescription
import uk.ac.warwick.camcat.sits.repositories.ModuleDescriptionRepository
import uk.ac.warwick.camcat.sits.repositories.ModuleRepository

interface ModuleService {
  fun findByModuleCode(code: String): Module?

  fun findDescriptions(moduleCode: String, academicYear: String): List<ModuleDescription>
}

@Service
class DatabaseModuleService(
  private val moduleRepository: ModuleRepository,
  private val descriptionRepository: ModuleDescriptionRepository
) : ModuleService {
  @Cacheable("moduleByCode")
  override fun findByModuleCode(code: String): Module? = moduleRepository.findById(code).orElse(null)

  @Cacheable("moduleDescriptionsByCodeAndAcademicYear")
  override fun findDescriptions(moduleCode: String, academicYear: String): List<ModuleDescription> =
    descriptionRepository.findAllByModuleCodeAndAcademicYear(moduleCode, academicYear)
}

