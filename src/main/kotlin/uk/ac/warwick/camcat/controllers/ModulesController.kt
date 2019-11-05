package uk.ac.warwick.camcat.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import uk.ac.warwick.camcat.sits.repositories.AssessmentTypeRepository
import uk.ac.warwick.camcat.sits.repositories.DepartmentRepository
import uk.ac.warwick.camcat.sits.repositories.FacultyRepository
import uk.ac.warwick.camcat.sits.repositories.LevelRepository
import uk.ac.warwick.util.termdates.AcademicYear
import java.math.BigDecimal

@Controller
@RequestMapping("/modules")
class ModulesController(
  private val departmentRepository: DepartmentRepository,
  private val facultyRepository: FacultyRepository,
  private val levelRepository: LevelRepository,
  private val assessmentTypeRepository: AssessmentTypeRepository
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

  @GetMapping
  fun index(@ModelAttribute("query") query: ModuleQuery): ModelAndView {
    return ModelAndView("modules/index")
  }
}

data class ModuleQuery(
  val code: String?,
  val keywords: String?,
  val department: String?,
  val faculty: String?,
  val level: String?,
  val leader: String?,
  val creditValue: BigDecimal?,
  val academicYear: AcademicYear?,
  val assessmentTypes: Set<String>?
)

