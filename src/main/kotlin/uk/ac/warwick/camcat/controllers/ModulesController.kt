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
import uk.ac.warwick.util.termdates.AcademicYear

@Controller
@RequestMapping("/modules")
class ModulesController(
  private val departmentRepository: DepartmentRepository,
  private val facultyRepository: FacultyRepository,
  private val levelRepository: LevelRepository,
  private val assessmentTypeRepository: AssessmentTypeRepository,
  private val moduleSearchService: ModuleSearchService
) {
  @ModelAttribute("departments")
  fun departments() = departmentRepository.findAllAcademicDepartments()

  @ModelAttribute("faculties")
  fun faculties() = facultyRepository.findAll()

  @ModelAttribute("levels")
  fun levels() = levelRepository.findAll()

  @ModelAttribute("academicYears")
  fun academicYears() = listOf(AcademicYear.starting(2019))

  @ModelAttribute("assessmentTypes")
  fun assessmentTypes() = assessmentTypeRepository.findAll()

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

