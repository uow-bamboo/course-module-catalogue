package uk.ac.warwick.camcat.system.context

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport
import org.springframework.data.elasticsearch.core.DefaultEntityMapper
import org.springframework.data.elasticsearch.core.EntityMapper
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentProperty
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories
import org.springframework.data.mapping.context.MappingContext

@Configuration
@EnableElasticsearchRepositories(basePackages = ["uk.ac.warwick.camcat.search"])
class SearchContext : ElasticsearchConfigurationSupport() {
  override fun entityMapper(): EntityMapper = KotlinDefaultEntityMapper(elasticsearchMappingContext())
}

class KotlinDefaultEntityMapper(
  context: MappingContext<out ElasticsearchPersistentEntity<*>, ElasticsearchPersistentProperty>
) : DefaultEntityMapper(context) {
  init {
    val field = DefaultEntityMapper::class.java.getDeclaredField("objectMapper")
    field.isAccessible = true

    val objectMapper = field.get(this) as ObjectMapper
    objectMapper.registerModule(KotlinModule())
  }
}
