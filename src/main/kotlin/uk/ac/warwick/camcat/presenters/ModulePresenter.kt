package uk.ac.warwick.camcat.presenters

import org.springframework.stereotype.Component
import uk.ac.warwick.camcat.sits.entities.Module
import uk.ac.warwick.camcat.sits.entities.ModuleDescription
import uk.ac.warwick.camcat.sits.entities.ModuleOccurrence

@Component
class ModulePresenterFactory(private val userPresenterFactory: UserPresenterFactory) {
  fun build(
    module: Module,
    occurrenceCollection: Collection<ModuleOccurrence>,
    descriptions: Collection<ModuleDescription>
  ) = ModulePresenter(
    module, occurrenceCollection, descriptions, userPresenterFactory
  )
}

class ModulePresenter(
  module: Module,
  occurrenceCollection: Collection<ModuleOccurrence>,
  descriptions: Collection<ModuleDescription>,
  userPresenterFactory: UserPresenterFactory
) {
  private val sortedOccurrences = occurrenceCollection.sortedBy { it.key.occurrence }
  private val primaryOccurrence = sortedOccurrences.find { it.key.occurrence == "A" } ?: sortedOccurrences.first()

  private val descriptionsByCode = descriptions.groupBy { it.code }.mapValues { it.value.sortedBy { it.key.sequence } }

  private fun descriptionText(code: String): String? = descriptionsByCode[code]?.firstOrNull()?.description
  private fun descriptionTextList(code: String): List<String> =
    descriptionsByCode[code]?.mapNotNull { it.description } ?: listOf()

  val code = module.code
  val stemCode = module.code.take(5)
  val name = module.name ?: "Untitled module"
  val creditValue = module.creditValue ?: module.code.takeLastWhile { it != '-' }

  val department = module.department
  val faculty = module.department?.faculty

  val moduleLeader =
    primaryOccurrence.moduleLeaderPersonnelCode?.let { userPresenterFactory.buildFromPersonnelCode(it) }

  val learningOutcomes: List<String> = descriptionTextList(code = "MA011")
  val introductoryDescription = descriptionText(code = "MA002")
  val moduleAims = descriptionText(code = "TMB003")
  val outlineSyllabus = descriptionText(code = "MA003")

  // TODO replace with presenter
  val occurrences = sortedOccurrences

  // TODO replace with presenter
  val assessmentComponents =
    module.assessmentPattern?.components
      ?.filter { it.inUse == true }
      ?.filter { it.assessmentGroup == module.assessmentPattern.defaultAssessmentGroup }
      ?.sortedBy { it.key.sequence }
      ?: listOf()
}

