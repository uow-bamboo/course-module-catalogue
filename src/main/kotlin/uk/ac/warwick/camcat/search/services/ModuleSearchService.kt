package uk.ac.warwick.camcat.search.services

import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.sort.SortBuilders
import org.elasticsearch.search.sort.SortOrder
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
import org.springframework.stereotype.Service
import uk.ac.warwick.camcat.search.documents.Module
import uk.ac.warwick.camcat.search.queries.ModuleQuery
import uk.ac.warwick.camcat.search.repositories.ModuleSearchRepository
import uk.ac.warwick.userlookup.UserLookup
import uk.ac.warwick.userlookup.UserLookupInterface

interface ModuleSearchService {
  fun query(query: ModuleQuery, page: Pageable): Page<Module>?
}

@Service
class ElasticsearchModuleSearchService(
  private val moduleSearchRepository: ModuleSearchRepository,
  private val userLookup: UserLookupInterface
) : ModuleSearchService {
  override fun query(query: ModuleQuery, page: Pageable): Page<Module>? {
    val boolQuery = QueryBuilders.boolQuery()

    if (!query.code.isNullOrBlank()) {
      boolQuery.must(
        QueryBuilders.wildcardQuery(
          "code",
          query.code.toUpperCase().filter { it.isLetterOrDigit() || it == '-' } + "*"))
    }

    if (!query.department.isNullOrBlank()) {
      boolQuery.must(QueryBuilders.termQuery("departmentCode", query.department))
    }

    if (!query.faculty.isNullOrBlank()) {
      boolQuery.must(QueryBuilders.termQuery("facultyCode", query.faculty))
    }

    if (!query.level.isNullOrBlank()) {
      boolQuery.must(QueryBuilders.termQuery("levelCode", query.level))
    }

    if (query.creditValue != null) {
      boolQuery.must(QueryBuilders.termQuery("creditValue", query.creditValue))
    }

    if (query.academicYear != null) {
      boolQuery.must(QueryBuilders.termQuery("academicYear", query.academicYear.startYear))
    }

    if (!query.keywords.isNullOrBlank()) {
      boolQuery.must(
        QueryBuilders.disMaxQuery()
          .add(QueryBuilders.matchQuery("title", query.keywords))
          .add(QueryBuilders.matchQuery("text", query.keywords))
      )
    }

    if (!query.leader.isNullOrBlank()) {
      val moduleLeader = userLookup.getUserByUserId(query.leader)

      if (moduleLeader.isFoundUser) {
        boolQuery.must(QueryBuilders.termQuery("occurrences.moduleLeader", moduleLeader.warwickId))
      }
    }

    if (boolQuery.hasClauses()) {
      val searchQuery = NativeSearchQueryBuilder()
        .withQuery(boolQuery)
        .withSort(SortBuilders.fieldSort("code").order(SortOrder.ASC))
        .withPageable(page)
        .build()

      return moduleSearchRepository.search(searchQuery)
    }

    return null
  }
}

