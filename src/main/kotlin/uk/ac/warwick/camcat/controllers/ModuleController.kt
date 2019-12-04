package uk.ac.warwick.camcat.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
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

  @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
  @ResponseBody
  @CrossOrigin
  fun showJson(@ModelAttribute("module", binding = false) module: ModulePresenter) = module
}
