package uk.ac.warwick.camcat.sits.services

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import uk.ac.warwick.camcat.sits.repositories.AssessmentTypeRepository
import uk.ac.warwick.camcat.system.cache.VariableTtlCacheDecorator

@Service
class AssessmentTypeService(
  private val repository: AssessmentTypeRepository,
  @Value("#{variableTtlCacheFactory.getCache('assessmentTypes')}") private val cache: VariableTtlCacheDecorator
) {
  fun findAll() = cache.get("all") { repository.findAll() }.orEmpty()
}
