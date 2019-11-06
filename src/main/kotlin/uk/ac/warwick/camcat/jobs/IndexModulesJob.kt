package uk.ac.warwick.camcat.jobs

import org.quartz.DisallowConcurrentExecution
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import uk.ac.warwick.camcat.presenters.ModulePresenterFactory
import uk.ac.warwick.camcat.search.documents.Module
import uk.ac.warwick.camcat.search.documents.ModuleOccurrence
import uk.ac.warwick.camcat.search.repositories.ModuleSearchRepository
import uk.ac.warwick.camcat.sits.services.ModuleService
import uk.ac.warwick.camcat.system.Logging
import uk.ac.warwick.util.termdates.AcademicYear
import java.time.LocalDate

@DisallowConcurrentExecution
class IndexModulesJob(
  private val moduleService: ModuleService,
  private val moduleSearchRepository: ModuleSearchRepository,
  private val modulePresenterFactory: ModulePresenterFactory
) : Job, Logging() {
  private val academicYears = mutableListOf(AcademicYear.starting(2019))

  init {
    val now = AcademicYear.forDate(LocalDate.now())

    while (academicYears.last().isBefore(now)) {
      academicYears.add(academicYears.last().next())
    }
  }

  override fun execute(context: JobExecutionContext?) {
    try {
      var pageRequest: Pageable = PageRequest.of(1, 50, Sort.by("code"))

      do {
        val page = moduleService.findAll(pageRequest)
        logger.info("Fetched ${page.size} modules from page ${pageRequest.pageNumber} of ${page.totalPages}")

        page.forEach { module ->
          academicYears.forEach { academicYear ->
            try {
              moduleSearchRepository.indexWithoutRefresh(createDocument(module, academicYear))
            } catch (e: Throwable) {
              logger.debug("Exception indexing Module for ${module.code} for $academicYear", e)
            }
          }
        }

        pageRequest = pageRequest.next()
      } while (page.hasNext())
    } finally {
      moduleSearchRepository.refresh()
    }
  }

  private fun createDocument(mod: uk.ac.warwick.camcat.sits.entities.Module, academicYear: AcademicYear): Module {
    val module = modulePresenterFactory.build(
      module = mod,
      occurrences = moduleService.findOccurrences(mod.code, academicYear),
      descriptions = moduleService.findDescriptions(mod.code, academicYear)
    )

    return Module(
      id = "${module.code}-$academicYear",
      code = module.code,
      academicYear = academicYear.startYear,
      title = module.title,
      stemCode = module.stemCode,
      creditValue = module.creditValue,
      departmentCode = module.department?.code,
      facultyCode = module.faculty?.code,
      levelCode = module.level?.code,
      occurrences = module.occurrences.map {
        ModuleOccurrence(
          moduleLeader = it.moduleLeader?.universityId,
          periodSlotCode = it.periodSlotCode,
          locationCode = it.location?.code
        )
      },
      text = listOfNotNull(
        *(module.learningOutcomes.toTypedArray()),
        module.introductoryDescription,
        module.moduleAims,
        module.outlineSyllabus
      ).joinToString(separator = "\n")
    )
  }
}
