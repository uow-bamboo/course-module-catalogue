package uk.ac.warwick.camcat.controllers

import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import uk.ac.warwick.camcat.search.documents.Module
import uk.ac.warwick.camcat.search.queries.ModuleQuery
import uk.ac.warwick.camcat.search.services.ModuleSearchService
import uk.ac.warwick.camcat.services.DepartmentService
import uk.ac.warwick.camcat.sits.repositories.AssessmentTypeRepository
import uk.ac.warwick.camcat.sits.repositories.LevelRepository
import uk.ac.warwick.camcat.sits.repositories.ModuleRepository
import uk.ac.warwick.util.termdates.AcademicYear
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
  @ModelAttribute("departments")
  fun departments() = departmentService.findAllDepartments()

  @ModelAttribute("faculties")
  fun faculties() = departmentService.findAllFaculties()

  @ModelAttribute("levels")
  fun levels() = levelRepository.findAll()

  @ModelAttribute("academicYears")
  fun academicYears() = listOf(
    AcademicYear.starting(2020)
  )

  @ModelAttribute("assessmentTypes")
  fun assessmentTypes() = assessmentTypeRepository.findAll()

  @ModelAttribute("creditValueOptions")
  fun creditValueOptions() = moduleRepository.findCreditValues()

  @ModelAttribute("results")
  fun results(@ModelAttribute("query") query: ModuleQuery? = null, page: Pageable): PageableModuleResults? {
    if (query == null) return null
    val serviceResult = moduleSearchService.query(query, page) ?: Page.empty()
    if (serviceResult.isEmpty) return null
    val currentPage = serviceResult.pageable.pageNumber
    val lastPage = serviceResult.totalPages - 1
    val pageRange = (max(currentPage - 5, 0)..min(currentPage + 5, lastPage)).toList()

    return PageableModuleResults(
      currentPage = currentPage,
      lastPage = lastPage,
      pageRange = pageRange,
      modules = serviceResult.toList()
    )
  }

  data class PageableModuleResults(
    val currentPage: Int,
    val lastPage: Int,
    val pageRange: List<Int>,
    val modules: List<Module>
  )

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

data class ModuleResult(
  val code: String,
  val academicYear: Int,
  val title: String,
  val departmentCode: String?
)
