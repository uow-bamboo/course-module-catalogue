package uk.ac.warwick.camcat.sits.services

import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import uk.ac.warwick.camcat.sits.entities.Level
import uk.ac.warwick.camcat.sits.repositories.LevelRepository

interface LevelService {
  fun findAll(): Iterable<Level>
}

@Service
class DatabaseLevelService(
  private val levelRepository: LevelRepository
) : LevelService {
  @Cacheable("levels")
  override fun findAll(): Iterable<Level> = levelRepository.findAll()
}

