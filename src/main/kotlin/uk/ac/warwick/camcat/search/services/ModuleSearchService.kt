package uk.ac.warwick.camcat.search.services

import org.apache.lucene.search.join.ScoreMode
import org.elasticsearch.index.query.Operator
import org.elasticsearch.index.query.QueryBuilders.*
import org.elasticsearch.search.sort.SortBuilders.fieldSort
import org.elasticsearch.search.sort.SortBuilders.scoreSort
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
import org.springframework.stereotype.Service
import uk.ac.warwick.camcat.search.documents.Module
import uk.ac.warwick.camcat.search.queries.ModuleQuery
import uk.ac.warwick.camcat.search.repositories.ModuleSearchRepository
import org.elasticsearch.index.query.SimpleQueryStringFlag as Flag

interface ModuleSearchService {
  fun query(query: ModuleQuery, page: Pageable = Pageable.unpaged()): Page<Module>?
}

@Service
class ElasticsearchModuleSearchService(private val moduleSearchRepository: ModuleSearchRepository) :
  ModuleSearchService {
  override fun query(query: ModuleQuery, page: Pageable): Page<Module>? {
    val boolQuery = boolQuery()

    boolQuery.must(termQuery("academicYear", query.academicYear.startYear))

    if (!query.department.isNullOrBlank()) {
      if (query.department.length == 1) {
        boolQuery.must(termQuery("facultyCode", query.department))
      } else {
        boolQuery.must(termQuery("departmentCode", query.department))
      }
    }

    if (!query.level.isNullOrBlank()) {
      boolQuery.must(termQuery("levelCode", query.level))
    }

    if (query.creditValue != null) {
      boolQuery.must(termQuery("creditValue", query.creditValue))
    }

    if (!query.assessmentType.isNullOrBlank()) {
      boolQuery.must(
        nestedQuery(
          "assessmentComponents",
          termQuery("assessmentComponents.typeCode", query.assessmentType),
          ScoreMode.Total
        )
      )
    }

    if (!query.keywords.isNullOrBlank()) {
      boolQuery.must(
        disMaxQuery()
          .add(wildcardQuery("code",
            query.keywords.toUpperCase().filter { it.isLetterOrDigit() || it == '-' } + "*").boost(100F))
          .add(
            simpleQueryStringQuery(query.keywords)
              .field("title", 10F)
              .field("departmentName", 2F)
              .field("facultyName")
              .field("text")
              .defaultOperator(Operator.AND)
              .flags(Flag.AND, Flag.OR, Flag.NOT, Flag.PHRASE, Flag.PRECEDENCE)
          )
          .add(matchPhraseQuery("leaderName", query.keywords).slop(2).boost(10F))
      )
    }

    if (boolQuery.hasClauses()) {
      val searchQuery = NativeSearchQueryBuilder()
        .withQuery(boolQuery)
        .withSort(scoreSort())
        .withSort(fieldSort("code"))
        .withPageable(page)
        .build()

      return moduleSearchRepository.search(searchQuery)
    }

    return null
  }
}

