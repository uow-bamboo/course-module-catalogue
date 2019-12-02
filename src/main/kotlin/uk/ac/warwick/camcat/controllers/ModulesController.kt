package uk.ac.warwick.camcat.controllers

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import uk.ac.warwick.camcat.search.documents.Module
import uk.ac.warwick.camcat.search.queries.ModuleQuery
import uk.ac.warwick.camcat.search.services.ModuleSearchService
import uk.ac.warwick.camcat.services.DepartmentService
import uk.ac.warwick.camcat.sits.repositories.AssessmentTypeRepository
import uk.ac.warwick.camcat.sits.repositories.LevelRepository
import uk.ac.warwick.camcat.sits.repositories.ModuleRepository
import uk.ac.warwick.util.termdates.AcademicYear

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
  fun results(@ModelAttribute("query") query: ModuleQuery? = null): List<Module>? {
    if (query != null) {
      return moduleSearchService.query(query, PageRequest.of(0, 50))?.toList()
    }

    return null
  }

  @GetMapping
  fun index() = ModelAndView("modules/index")
}

