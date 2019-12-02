package uk.ac.warwick.camcat.controllers

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.ModelAndView
import uk.ac.warwick.camcat.presenters.ModulePresenter
import uk.ac.warwick.camcat.presenters.ModulePresenterFactory
import uk.ac.warwick.util.termdates.AcademicYear

@Controller
@RequestMapping("/modules/{academicYear}/{moduleCode}")
class ModuleController(
  private val modulePresenterFactory: ModulePresenterFactory
) {
  @ModelAttribute("module")
  fun module(@PathVariable("moduleCode") moduleCode: String, @PathVariable("academicYear") academicYear: AcademicYear): ModulePresenter =
    modulePresenterFactory.build(moduleCode, academicYear) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

  @GetMapping
  fun show() = ModelAndView("modules/show")
}
