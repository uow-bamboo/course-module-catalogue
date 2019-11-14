package uk.ac.warwick.camcat.sits.services

import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import uk.ac.warwick.camcat.sits.entities.AssessmentType
import uk.ac.warwick.camcat.sits.repositories.AssessmentTypeRepository

interface AssessmentTypeService {
  fun findAll(): Iterable<AssessmentType>
}

@Service
class DatabaseAssessmentTypeService(
  private val assessmentTypeRepository: AssessmentTypeRepository
) : AssessmentTypeService {
  @Cacheable("assessmentTypes")
  override fun findAll(): Iterable<AssessmentType> = assessmentTypeRepository.findAll()
}

