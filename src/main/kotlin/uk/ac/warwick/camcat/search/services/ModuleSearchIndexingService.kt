package uk.ac.warwick.camcat.search.services

import org.springframework.stereotype.Service
import uk.ac.warwick.camcat.presenters.ModulePresenterFactory
import uk.ac.warwick.camcat.search.documents.AssessmentComponent
import uk.ac.warwick.camcat.search.repositories.ModuleSearchRepository
import uk.ac.warwick.camcat.sits.services.ModuleService
import uk.ac.warwick.camcat.system.Logging
import uk.ac.warwick.util.termdates.AcademicYear
import uk.ac.warwick.camcat.search.documents.Module as ModuleDocument
import uk.ac.warwick.camcat.sits.entities.Module as ModuleEntity

interface ModuleSearchIndexingService {
  fun indexModules()
}

@Service
class ElasticsearchModuleSearchIndexingService(
  private val moduleService: ModuleService,
  private val moduleSearchRepository: ModuleSearchRepository,
  private val modulePresenterFactory: ModulePresenterFactory
) : ModuleSearchIndexingService, Logging() {
  private val academicYears = mutableListOf(
    AcademicYear.starting(2020)
  )

  override fun indexModules() {
    try {
      academicYears.forEach { academicYear ->
        val stream = moduleService.findAllWithOccurrenceInAcademicYear(academicYear)

        stream.forEach { module ->
          logger.debug("Indexing ${module.code} ${module.title} for $academicYear")
          try {
            moduleSearchRepository.indexWithoutRefresh(createDocument(module, academicYear))
          } catch (e: Throwable) {
            logger.debug("Exception indexing Module for ${module.code} for $academicYear", e)
          }
        }
      }
    } finally {
      moduleSearchRepository.refresh()
    }
  }

  private fun createDocument(mod: ModuleEntity, academicYear: AcademicYear): ModuleDocument {
    val module = modulePresenterFactory.build(mod.code, academicYear)!!

    return ModuleDocument(
      id = "${module.code}-${academicYear.startYear}",
      code = module.code,
      academicYear = academicYear.startYear,
      title = module.title,
      stemCode = module.stemCode,
      creditValue = module.creditValue,
      departmentCode = module.department?.code,
      departmentShortName = module.department?.shortName,
      departmentName = module.department?.name,
      facultyCode = module.faculty?.code,
      facultyName = module.faculty?.name,
      levelCode = module.level?.code,
      leader = module.leader?.universityId,
      leaderName = module.leader?.name,
      text = listOfNotNull(
        module.introductoryDescription,
        module.aims,
        module.outlineSyllabus,
        *(module.learningOutcomes.toTypedArray()),
        module.subjectSpecificSkills,
        module.transferableSkills
      ).joinToString(separator = "\n"),
      assessmentComponents = module.assessmentGroups.flatMap { group ->
        group.components.filter { it.typeCode != null }.map { component ->
          AssessmentComponent(
            typeCode = component.typeCode!!,
            name = component.name
          )
        }
      }
    )
  }
}
