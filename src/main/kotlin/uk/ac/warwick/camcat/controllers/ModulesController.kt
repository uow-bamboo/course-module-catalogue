package uk.ac.warwick.camcat.controllers

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import uk.ac.warwick.camcat.search.documents.Module
import uk.ac.warwick.camcat.search.queries.ModuleQuery
import uk.ac.warwick.camcat.search.services.ModuleSearchService
import uk.ac.warwick.camcat.sits.repositories.AssessmentTypeRepository
import uk.ac.warwick.camcat.sits.repositories.DepartmentRepository
import uk.ac.warwick.camcat.sits.repositories.FacultyRepository
import uk.ac.warwick.camcat.sits.repositories.LevelRepository
import uk.ac.warwick.camcat.sits.services.AssessmentTypeService
import uk.ac.warwick.camcat.sits.services.DepartmentService
import uk.ac.warwick.camcat.sits.services.FacultyService
import uk.ac.warwick.camcat.sits.services.LevelService
import uk.ac.warwick.util.termdates.AcademicYear

@Controller
@RequestMapping("/modules")
class ModulesController(
  private val departmentService: DepartmentService,
  private val facultyService: FacultyService,
  private val levelService: LevelService,
  private val assessmentTypeService: AssessmentTypeService,
  private val moduleSearchService: ModuleSearchService
) {
  @ModelAttribute("departments")
  fun departments() = departmentService.findAllAcademic()

  @ModelAttribute("faculties")
  fun faculties() = facultyService.findAll()

  @ModelAttribute("levels")
  fun levels() = levelService.findAll()

  @ModelAttribute("academicYears")
  fun academicYears() = listOf(AcademicYear.starting(2019))

  @ModelAttribute("assessmentTypes")
  fun assessmentTypes() = assessmentTypeService.findAll()

  @ModelAttribute("results")
  fun results(@ModelAttribute("query") query: ModuleQuery? = null, page: Pageable): List<Module>? {
    if (query != null) {
      return moduleSearchService.query(query, page)?.toList()
    }

    return null
  }

  @GetMapping
  fun index() = ModelAndView("modules/index")
}

