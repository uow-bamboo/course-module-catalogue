package uk.ac.warwick.camcat.controllers

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.view.RedirectView
import uk.ac.warwick.camcat.sits.services.ModuleService
import uk.ac.warwick.util.termdates.AcademicYear
import java.time.LocalDate

@Controller
class ShortUrlController(
  private val moduleService: ModuleService
) {
  companion object {
    const val moduleStemCode = "{moduleStemCode:[a-zA-Z]{2}[a-zA-Z\\d]{3}}"
    const val moduleCode = "{moduleCode:[a-zA-Z]{2}[a-zA-Z\\d]{3}-[\\d.]{1,3}}"
    const val academicYear = "{academicYear:\\d{2}(?:\\d{2})?}"
    val yearZero: AcademicYear = AcademicYear.starting(2020)
  }

  @RequestMapping("/$moduleStemCode")
  fun moduleByStemCode(@PathVariable("moduleStemCode") stemCode: String): RedirectView {
    val code = moduleService.findAllCodesByStemCode(stemCode.toUpperCase()).firstOrNull()
      ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    return moduleByCode(code)
  }

  @RequestMapping("/$moduleCode")
  fun moduleByCode(@PathVariable("moduleCode") code: String): RedirectView {
    val now = AcademicYear.forDate(LocalDate.now())

    return moduleByAcademicYearAndCode(now.takeUnless { it.isBefore(yearZero) } ?: yearZero, code.toUpperCase())
  }

  @RequestMapping("/$academicYear/$moduleStemCode")
  fun moduleByStemCode(@PathVariable("academicYear") academicYear: String, @PathVariable("moduleStemCode") stemCode: String): RedirectView {
    val code = moduleService.findAllCodesByStemCode(stemCode.toUpperCase()).firstOrNull()
      ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    return moduleByAcademicYearAndCode(academicYear, code)
  }

  @RequestMapping("/$academicYear/$moduleCode")
  fun moduleByAcademicYearAndCode(@PathVariable("academicYear") academicYear: String, @PathVariable("moduleCode") code: String) =
    moduleByAcademicYearAndCode(parseAcademicYear(academicYear), code.toUpperCase())

  private fun moduleByAcademicYearAndCode(academicYear: AcademicYear, code: String) =
    RedirectView("/modules/${academicYear.startYear}/$code")

  private fun parseAcademicYear(string: String): AcademicYear = when (string.length) {
    2 -> AcademicYear.starting("20$string".toInt())
    4 -> AcademicYear.starting(string.toInt())
    else -> throw ResponseStatusException(HttpStatus.BAD_REQUEST)
  }
}
