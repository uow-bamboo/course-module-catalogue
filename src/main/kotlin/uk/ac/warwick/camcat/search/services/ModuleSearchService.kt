package uk.ac.warwick.camcat.search.services

import org.apache.lucene.search.join.ScoreMode
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.index.query.Operator
import org.elasticsearch.index.query.QueryBuilders.*
import org.elasticsearch.search.aggregations.AggregationBuilders.nested
import org.elasticsearch.search.aggregations.AggregationBuilders.terms
import org.elasticsearch.search.aggregations.Aggregations
import org.elasticsearch.search.aggregations.bucket.nested.Nested
import org.elasticsearch.search.aggregations.bucket.terms.Terms
import org.elasticsearch.search.sort.SortBuilders.fieldSort
import org.elasticsearch.search.sort.SortBuilders.scoreSort
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.ResultsMapper
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
import org.springframework.data.elasticsearch.core.query.SearchQuery
import org.springframework.stereotype.Service
import uk.ac.warwick.camcat.search.documents.Module
import uk.ac.warwick.camcat.search.queries.ModuleQuery

interface ModuleSearchService {
  fun query(query: ModuleQuery, page: Pageable = Pageable.unpaged()): ModuleSearchResult
}

@Service
class ElasticsearchModuleSearchService(
  private val elasticsearch: ElasticsearchOperations,
  private val resultsMapper: ResultsMapper
) :
  ModuleSearchService {
  override fun query(query: ModuleQuery, page: Pageable): ModuleSearchResult {
    fun queryForTerms(query: ModuleQuery, aggregationName: String): Collection<String> =
      elasticsearch.query(buildQuery(query, page)) { response -> terms(aggregationName, response) }

    val searchQuery = buildQuery(query, page)

    var result = elasticsearch.query(searchQuery) { searchResponse ->
      ModuleSearchResult(
        page = resultsMapper.mapResults(searchResponse, Module::class.java, page),
        assessmentTypeCodes = terms("assessment_components.type_codes", searchResponse),
        departmentCodes = terms("department_codes", searchResponse),
        levelCodes = terms("level_codes", searchResponse),
        creditValues = terms("credit_values", searchResponse)
      )
    }

    if (!query.department.isNullOrBlank()) {
      result = result.copy(departmentCodes = queryForTerms(query.copy(department = null), "department_codes"))
    }

    if (!query.assessmentType.isNullOrBlank()) {
      result = result.copy(
        assessmentTypeCodes = queryForTerms(query.copy(assessmentType = null), "assessment_components.type_codes")
      )
    }

    if (!query.level.isNullOrBlank()) {
      result = result.copy(levelCodes = queryForTerms(query.copy(level = null), "level_codes"))
    }

    if (query.creditValue != null) {
      result = result.copy(creditValues = queryForTerms(query.copy(creditValue = null), "credit_values"))
    }

    return result
  }

  private fun terms(aggregationName: String, searchResponse: SearchResponse): Collection<String> {
    val path = aggregationName.split(".")

    val aggregations = path.dropLast(1).fold<String, Aggregations>(searchResponse.aggregations) { aggregations, it ->
      aggregations.get<Nested>(it).aggregations
    }

    return aggregations.get<Terms>(path.last()).buckets.map { it.keyAsString }
  }

  private fun buildQuery(query: ModuleQuery, page: Pageable): SearchQuery {
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
          )
          .add(matchPhraseQuery("leaderName", query.keywords).slop(2).boost(10F))
      )
    }

    return NativeSearchQueryBuilder()
      .withQuery(boolQuery)
      .withSort(scoreSort())
      .withSort(fieldSort("code"))
      .withPageable(page)
      .addAggregation(
        nested("assessment_components", "assessmentComponents")
          .subAggregation(terms("type_codes").field("assessmentComponents.typeCode").size(1000))
      )
      .addAggregation(terms("department_codes").field("departmentCode").size(1000))
      .addAggregation(terms("level_codes").field("levelCode").size(1000))
      .addAggregation(terms("credit_values").field("creditValue").size(1000))
      .build()
  }
}

data class ModuleSearchResult(
  val page: Page<Module>,
  val assessmentTypeCodes: Collection<String>,
  val departmentCodes: Collection<String>,
  val levelCodes: Collection<String>,
  val creditValues: Collection<String>
)

