package uk.ac.warwick.camcat.controllers

import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import uk.ac.warwick.camcat.search.documents.Module
import uk.ac.warwick.camcat.search.queries.ModuleQuery
import uk.ac.warwick.camcat.search.services.ModuleSearchResult
import uk.ac.warwick.camcat.search.services.ModuleSearchService
import uk.ac.warwick.camcat.services.Department
import uk.ac.warwick.camcat.services.DepartmentService
import uk.ac.warwick.camcat.services.Faculty
import uk.ac.warwick.camcat.sits.entities.AssessmentType
import uk.ac.warwick.camcat.sits.entities.Level
import uk.ac.warwick.camcat.sits.repositories.AssessmentTypeRepository
import uk.ac.warwick.camcat.sits.repositories.LevelRepository
import uk.ac.warwick.camcat.sits.repositories.ModuleRepository
import uk.ac.warwick.util.termdates.AcademicYear
import java.math.BigDecimal
import kotlin.math.max
import kotlin.math.min

@Controller
@RequestMapping("/modules")
class ModulesController(
  private val departmentService: DepartmentService,
  private val levelRepository: LevelRepository,
  private val assessmentTypeRepository: AssessmentTypeRepository,
  private val moduleSearchService: ModuleSearchService,
  private val moduleRepository: ModuleRepository
) {
  @ModelAttribute("academicYears")
  fun academicYears() = listOf(
    AcademicYear.starting(2020)
  )

  @ModelAttribute("results")
  fun results(@ModelAttribute("query") query: ModuleQuery? = null, page: Pageable): PageableModuleResults? {
    if (query == null) return null
    val result = moduleSearchService.query(query, page) ?: return null

    val currentPage = result.page.pageable.pageNumber
    val lastPage = result.page.totalPages - 1
    val pageRange = (max(currentPage - 5, 0)..min(currentPage + 5, lastPage)).toList()

    return PageableModuleResults(
      currentPage = currentPage,
      lastPage = lastPage,
      pageRange = pageRange,
      modules = result.page.toList(),
      result = result,
      filterOptions = getFilterOptions(result)
    )
  }

  fun getFilterOptions(result: ModuleSearchResult?): FilterOptions {
    val departments = if (result != null) {
      departmentService.findAllDepartments().filter { result.departmentCodes.contains(it.code) }
    } else {
      departmentService.findAllDepartments()
    }

    val faculties = departmentService.findAllFaculties().filter { f -> departments.any { d -> d.faculty == f } }

    val assessmentTypes = if (result != null) {
      assessmentTypeRepository.findAll().filter { result.assessmentTypeCodes.contains(it.code) }
    } else {
      assessmentTypeRepository.findAll().toList()
    }

    val levels = if (result != null) {
      levelRepository.findAll().filter { result.levelCodes.contains(it.code) }
    } else {
      levelRepository.findAll().toList()
    }

    val creditValues = if (result != null) {
      moduleRepository.findCreditValues().filter { result.creditValues.contains(it.toString()) }
    } else {
      moduleRepository.findCreditValues()
    }

    return FilterOptions(
      faculties = faculties,
      departments = departments,
      assessmentTypes = assessmentTypes,
      levels = levels,
      creditValues = creditValues
    )
  }

  @GetMapping
  fun index() = ModelAndView("modules/index")

  @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
  @ResponseBody
  @CrossOrigin
  fun indexJson(@ModelAttribute("results", binding = false) results: List<Module>?) =
    results.orEmpty().map { module ->
      ModuleResult(
        code = module.code,
        academicYear = module.academicYear,
        title = module.title,
        departmentCode = module.departmentCode
      )
    }
}

data class PageableModuleResults(
  val currentPage: Int,
  val lastPage: Int,
  val pageRange: List<Int>,
  val modules: List<Module>,
  val result: ModuleSearchResult,
  val filterOptions: FilterOptions
)

data class FilterOptions(
  val faculties: List<Faculty>,
  val departments: List<Department>,
  val assessmentTypes: List<AssessmentType>,
  val levels: List<Level>,
  val creditValues: List<BigDecimal>
)

data class ModuleResult(
  val code: String,
  val academicYear: Int,
  val title: String,
  val departmentCode: String?
)
