package uk.ac.warwick.camcat.controllers.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import uk.ac.warwick.camcat.presenters.ModulePresenter
import uk.ac.warwick.camcat.presenters.ModulePresenterFactory
import uk.ac.warwick.camcat.sits.entities.Module
import uk.ac.warwick.camcat.sits.entities.ModuleDescription
import uk.ac.warwick.camcat.sits.entities.ModuleOccurrence
import uk.ac.warwick.camcat.sits.services.ModuleService
import uk.ac.warwick.util.termdates.AcademicYear

@RestController("apiModulesController")
@RequestMapping("/api/v1/modules/{moduleCode}", produces = ["application/json"])
class ModulesController(private val moduleService: ModuleService) {
  @GetMapping
  fun module(@PathVariable moduleCode: String): Module? =
    moduleService.findByModuleCode(moduleCode)
}

@RestController("apiModuleYearsController")
@RequestMapping("/api/v1/modules/{moduleCode}/{academicYear}", produces = ["application/json"])
class ModuleYearsController(
  private val moduleService: ModuleService,
  private val modulePresenterFactory: ModulePresenterFactory
) {
  @GetMapping
  fun occurrences(@PathVariable moduleCode: String, @PathVariable academicYear: AcademicYear): Collection<ModuleOccurrence> =
    moduleService.findOccurrences(moduleCode, academicYear)

  @GetMapping("/descriptions")
  fun descriptions(@PathVariable moduleCode: String, @PathVariable academicYear: AcademicYear): Collection<ModuleDescription> =
    moduleService.findDescriptions(moduleCode, academicYear)

  @GetMapping("/present")
  fun presentModule(@PathVariable moduleCode: String, @PathVariable academicYear: AcademicYear): ModulePresenter =
    modulePresenterFactory.build(moduleCode, academicYear) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
}
