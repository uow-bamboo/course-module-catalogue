package uk.ac.warwick.camcat.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import uk.ac.warwick.camcat.services.DepartmentService

@Controller
@RequestMapping("/")
class HomeController(private val departmentService: DepartmentService) {
  @ModelAttribute("departments")
  fun departments() = departmentService.findAllDepartments()

  @ModelAttribute("faculties")
  fun faculties() = departmentService.findAllFaculties()

  @GetMapping
  fun home() = ModelAndView("home")
}
