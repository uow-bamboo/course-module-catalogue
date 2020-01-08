package uk.ac.warwick.camcat.controllers

import org.springframework.http.HttpHeaders.CONTENT_DISPOSITION
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.ModelAndView
import uk.ac.warwick.camcat.presenters.ModulePresenter
import uk.ac.warwick.camcat.presenters.ModulePresenterFactory
import uk.ac.warwick.camcat.services.PdfService
import uk.ac.warwick.util.termdates.AcademicYear
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/modules/{academicYear}/{moduleCode}")
class ModuleController(
  private val modulePresenterFactory: ModulePresenterFactory,
  private val pdfService: PdfService
) {
  @ModelAttribute("module")
  fun module(@PathVariable("moduleCode") moduleCode: String, @PathVariable("academicYear") academicYear: AcademicYear): ModulePresenter =
    modulePresenterFactory.build(moduleCode, academicYear)
      ?: throw ModuleNotFoundResponseStatusException(moduleCode, academicYear)

  @GetMapping
  fun show() = ModelAndView("modules/show")

  @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
  @ResponseBody
  @CrossOrigin
  fun showJson(@ModelAttribute("module", binding = false) module: ModulePresenter) = module

  @GetMapping(produces = [MediaType.APPLICATION_PDF_VALUE])
  fun showPdf(
    @PathVariable("academicYear") academicYear: AcademicYear,
    @ModelAttribute("module", binding = false) module: ModulePresenter,
    response: HttpServletResponse
  ) {
    response.setHeader(CONTENT_DISPOSITION, "attachment; filename=${module.code}-${academicYear.startYear}.pdf")
    pdfService.render(
      "modules/show.pdf",
      mapOf("module" to module, "academicYear" to academicYear),
      response.outputStream
    )
  }
}

class ModuleNotFoundResponseStatusException(val moduleCode: String, val academicYear: AcademicYear) : ResponseStatusException(HttpStatus.NOT_FOUND)
