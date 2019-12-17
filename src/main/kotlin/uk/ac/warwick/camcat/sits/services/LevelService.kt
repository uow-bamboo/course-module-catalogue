package uk.ac.warwick.camcat.sits.services

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import uk.ac.warwick.camcat.sits.repositories.LevelRepository
import uk.ac.warwick.camcat.system.cache.VariableTtlCacheDecorator

@Service
class LevelService(
  private val repository: LevelRepository,
  @Value("#{variableTtlCacheFactory.getCache('levels')}") private val cache: VariableTtlCacheDecorator
) {
  fun findAll() = cache.get("all") { repository.findAll() }.orEmpty()
}
