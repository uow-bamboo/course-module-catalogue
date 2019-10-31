package uk.ac.warwick.camcat.search.repositories

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository
import uk.ac.warwick.camcat.search.documents.Module

@Repository
interface ModuleSearchRepository : ElasticsearchRepository<Module, String>
