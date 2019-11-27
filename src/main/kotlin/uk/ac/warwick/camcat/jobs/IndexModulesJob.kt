package uk.ac.warwick.camcat.jobs

import org.quartz.DisallowConcurrentExecution
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import uk.ac.warwick.camcat.presenters.ModulePresenterFactory
import uk.ac.warwick.camcat.search.documents.Module
import uk.ac.warwick.camcat.search.repositories.ModuleSearchRepository
import uk.ac.warwick.camcat.sits.services.ModuleService
import uk.ac.warwick.camcat.system.Logging
import uk.ac.warwick.util.termdates.AcademicYear
import java.time.LocalDate
import uk.ac.warwick.camcat.sits.entities.Module as SitsModule

@DisallowConcurrentExecution
class IndexModulesJob(
  private val moduleService: ModuleService,
  private val moduleSearchRepository: ModuleSearchRepository,
  private val modulePresenterFactory: ModulePresenterFactory
) : Job, Logging() {
  private val academicYears = mutableListOf(
    AcademicYear.starting(2019),
    AcademicYear.starting(2020)
  )

  init {
    val now = AcademicYear.forDate(LocalDate.now())

    while (academicYears.last().isBefore(now)) {
      academicYears.add(academicYears.last().next())
    }
  }

  override fun execute(context: JobExecutionContext?) {
    try {
      var pageRequest: Pageable = PageRequest.of(0, 50, Sort.by("code"))

      do {
        val page = moduleService.findAll(pageRequest)
        logger.info("Fetched ${page.numberOfElements} modules from page ${pageRequest.pageNumber + 1} of ${page.totalPages}")

        page.forEach { module ->
          logger.debug("Indexing ${module.code} ${module.title}")
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

  private fun createDocument(mod: SitsModule, academicYear: AcademicYear): Module {
    val module = modulePresenterFactory.build(mod.code, academicYear)!!

    return Module(
      id = "${module.code}-$academicYear",
      code = module.code,
      academicYear = academicYear.startYear,
      title = module.title,
      stemCode = module.stemCode,
      creditValue = module.creditValue,
      departmentCode = module.department?.code,
      departmentName = module.department?.shortName,
      facultyCode = module.faculty?.code,
      levelCode = module.level?.code,
      leader = module.leader?.universityId,
      text = listOfNotNull(
        *(module.learningOutcomes.toTypedArray()),
        module.introductoryDescription,
        module.aims,
        module.outlineSyllabus
      ).joinToString(separator = "\n")
    )
  }
}
