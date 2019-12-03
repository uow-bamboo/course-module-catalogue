package uk.ac.warwick.camcat.search.services

import org.apache.lucene.search.join.ScoreMode
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
import org.elasticsearch.index.query.SimpleQueryStringFlag as Flag

interface ModuleSearchService {
  fun query(query: ModuleQuery, page: Pageable = Pageable.unpaged()): ModuleSearchResult?
}

@Service
class ElasticsearchModuleSearchService(
  private val elasticsearch: ElasticsearchOperations,
  private val resultsMapper: ResultsMapper
) :
  ModuleSearchService {
  override fun query(query: ModuleQuery, page: Pageable): ModuleSearchResult? {
    val searchQuery = buildQuery(query, page)

    fun terms(aggregationName: String, aggregations: Aggregations): Collection<String> =
      aggregations.get<Terms>(aggregationName).buckets.map { it.keyAsString }

    if (searchQuery != null) {
      var result = elasticsearch.query(searchQuery) { searchResponse ->
        val assessmentComponentsAggregations = searchResponse.aggregations.get<Nested>("assessment_components").aggregations

        ModuleSearchResult(
          page = resultsMapper.mapResults(searchResponse, Module::class.java, page),
          assessmentTypeCodes = terms("type_codes", assessmentComponentsAggregations),
          departmentCodes = terms("department_codes", searchResponse.aggregations),
          levelCodes = terms("level_codes", searchResponse.aggregations),
          creditValues = terms("credit_values", searchResponse.aggregations)
        )
      }

      if (!query.department.isNullOrBlank()) {
        result = result.copy(departmentCodes = elasticsearch.query(buildQuery(query.copy(department = null), page)) { searchResponse ->
          terms("department_codes", searchResponse.aggregations)
        })
      }

      if (!query.assessmentType.isNullOrBlank()) {
        result = result.copy(assessmentTypeCodes = elasticsearch.query(buildQuery(query.copy(assessmentType = null), page)) { searchResponse ->
          val assessmentComponentsAggregations = searchResponse.aggregations.get<Nested>("assessment_components").aggregations
          terms("type_codes", assessmentComponentsAggregations)
        })
      }

      if (!query.level.isNullOrBlank()) {
        result = result.copy(levelCodes = elasticsearch.query(buildQuery(query.copy(level = null), page)) { searchResponse ->
          terms("level_codes", searchResponse.aggregations)
        })
      }

      if (query.creditValue != null) {
        result = result.copy(creditValues = elasticsearch.query(buildQuery(query.copy(creditValue = null), page)) { searchResponse ->
          terms("credit_values", searchResponse.aggregations)
        })
      }

      return result
    }

    return null
  }

  fun buildQuery(query: ModuleQuery, page: Pageable): SearchQuery? {
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
      return NativeSearchQueryBuilder()
        .withQuery(boolQuery)
        .withSort(scoreSort())
        .withSort(fieldSort("code"))
        .withPageable(page)
        .addAggregation(
          nested("assessment_components", "assessmentComponents")
            .subAggregation(terms("type_codes").field("assessmentComponents.typeCode").size(1000))
        )
        .addAggregation(
          terms("department_codes").field("departmentCode").size(1000)
        )
        .addAggregation(
          terms("level_codes").field("levelCode").size(1000)
        )
        .addAggregation(
          terms("credit_values").field("creditValue").size(1000)
        )
        .build()
    }

    return null
  }
}

data class ModuleSearchResult(
  val page: Page<Module>,
  val assessmentTypeCodes: Collection<String>,
  val departmentCodes: Collection<String>,
  val levelCodes: Collection<String>,
  val creditValues: Collection<String>
)

