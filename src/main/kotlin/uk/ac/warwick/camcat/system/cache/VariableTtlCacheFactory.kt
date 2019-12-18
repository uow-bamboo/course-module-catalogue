package uk.ac.warwick.camcat.system.cache

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
class VariableTtlCacheFactory(private val cacheManager: CacheManager, private val objectMapper: ObjectMapper) {
  fun getCache(name: String): VariableTtlCacheDecorator =
    VariableTtlCacheDecorator(cacheManager.getCache(name)!!, objectMapper)
}

