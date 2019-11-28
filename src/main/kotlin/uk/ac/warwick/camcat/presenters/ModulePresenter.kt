package uk.ac.warwick.camcat.presenters

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.stereotype.Component
import uk.ac.warwick.camcat.helpers.DurationFormatter
import uk.ac.warwick.camcat.services.WarwickDepartmentsService
import uk.ac.warwick.camcat.sits.entities.*
import uk.ac.warwick.camcat.sits.repositories.ModuleAvailability
import uk.ac.warwick.camcat.sits.services.ModuleService
import uk.ac.warwick.camcat.sits.services.RelatedModules
import uk.ac.warwick.util.termdates.AcademicYear
import java.math.BigDecimal
import java.time.Duration

@Component
class ModulePresenterFactory(
  private val userPresenterFactory: UserPresenterFactory,
  private val warwickDepartmentsService: WarwickDepartmentsService,
  private val moduleService: ModuleService
) {
  fun build(moduleCode: String, academicYear: AcademicYear): ModulePresenter? =
    moduleService.findByModuleCode(moduleCode)?.let { module ->
      ModulePresenter(
        module = module,
        occurrenceCollection = moduleService.findOccurrences(module.code, academicYear),
        descriptions = moduleService.findDescriptions(module.code, academicYear),
        relatedModules = moduleService.findRelatedModules(module.code, academicYear),
        topics = moduleService.findTopics(module.code, academicYear),
        availability = moduleService.findAvailability(module.code, academicYear),
        userPresenterFactory = userPresenterFactory,
        warwickDepartmentsService = warwickDepartmentsService
      )
    }
}

class ModulePresenter(
  module: Module,
  occurrenceCollection: Collection<ModuleOccurrence>,
  descriptions: Collection<ModuleDescription>,
  relatedModules: RelatedModules,
  topics: Collection<Topic>,
  availability: Collection<ModuleAvailability>,
  userPresenterFactory: UserPresenterFactory,
  warwickDepartmentsService: WarwickDepartmentsService
) {
  private val sortedOccurrences = occurrenceCollection.sortedBy { it.key.occurrenceCode }
  private val primaryOccurrence = sortedOccurrences.find { it.key.occurrenceCode == "A" } ?: sortedOccurrences.firstOrNull()

  private val descriptionsByCode = descriptions.groupBy { it.code }.mapValues { it.value.sortedBy { it.key.sequence } }

  private fun descriptions(code: String): List<ModuleDescription> = descriptionsByCode[code].orEmpty()
  private fun description(code: String): ModuleDescription? = descriptions(code).firstOrNull()
  private fun descriptionText(code: String): String? = description(code)?.description

  val code = module.code
  val stemCode = module.code.take(5)
  val title = module.title ?: "Untitled module"
  val creditValue = module.creditValue ?: module.code.takeLastWhile { it != '-' }.let(::BigDecimal)

  val department = module.department?.let { department ->
    DepartmentPresenter(
      department,
      warwickDepartmentsService.findByDepartmentCode(department.code)
    )
  }
  val faculty = module.department?.faculty

  val level = primaryOccurrence?.level
  val leader = primaryOccurrence?.moduleLeaderPersonnelCode?.let(userPresenterFactory::buildFromPersonnelCode)

  val duration = "Not yet in SITS" // TODO MA-634
  val locations = descriptions("MA010").map(::StudyLocation)
    .sortedWith(compareBy(StudyLocation::primary).reversed().thenBy(StudyLocation::name))

  val aims = descriptionText("TMB003")
  val assessmentFeedback: String? = null // TODO MA-635
  val indicativeReadingList = descriptionText("MA004")
  val interdisciplinary = descriptionText("MA008")
  val international = descriptionText("MA009")
  val introductoryDescription = descriptionText("MA002")
  val learningOutcomes = descriptions("MA011").mapNotNull { it.description }
  val mustPassAllAssessmentComponents = true // TODO MA-636
  val otherActivityDescription = descriptionText("MA023")
  val outlineSyllabus = descriptionText("MA003")
  val privateStudyDescription = descriptionText("MA026")
  val readingListUrl = description("MA004")?.title
  val researchElement = descriptionText("MA007")
  val subjectSpecificSkills = descriptionText("MA005")
  val transferableSkills = descriptionText("MA006")
  val url = descriptionText("TMB005")

  val studyAmounts = listOfNotNull(
    description("MA013")?.let { SessionStudyAmount("Lectures", it) },
    description("MA014")?.let { SessionStudyAmount("Seminars", it) },
    description("MA015")?.let { SessionStudyAmount("Tutorials", it) },
    description("MA016")?.let { SessionStudyAmount("Project supervision", it) },
    description("MA017")?.let { SessionStudyAmount("Demonstrations", it) },
    description("MA018")?.let { SessionStudyAmount("Practical classes", it) },
    description("MA019")?.let { SessionStudyAmount("Supervised practical classes", it) },
    description("MA020")?.let { SessionStudyAmount("Fieldwork", it) },
    description("MA021")?.let { SessionStudyAmount("External visits", it) },
    description("MA022")?.let { SessionStudyAmount("Work-based learning", it) },
    description("MA026")?.let {
      DurationStudyAmount(
        "Private study",
        Duration.ofHours(BigDecimal(it.title).longValueExact())
      )
    }
  ).filterNot { it.zero }

  val preRequisiteModules = relatedModules.preRequisites.map(::AssociatedModulePresenter)
  val postRequisiteModules = relatedModules.postRequisites.map(::AssociatedModulePresenter)
  val antiRequisiteModules = relatedModules.antiRequisites.map(::AssociatedModulePresenter)

  val assessmentGroups =
    module.assessmentPattern?.components
      ?.filterNot { it.assessmentGroup == null || it.assessmentGroup == "AO" }
      ?.groupBy { it.assessmentGroup }
      ?.map { AssessmentGroupPresenter(module.assessmentPattern, it.value) }
      ?.sortedWith(compareBy(AssessmentGroupPresenter::default).reversed().thenBy(AssessmentGroupPresenter::name))
      ?: listOf()

  val costs = descriptions("MA031").map(::ModuleCost)

  val hasAuditOnlyAssessmentGroup = module.assessmentPattern?.components?.any { it.assessmentGroup == "AO" }

  val teachingSplits = topics.filter { it.teachingDepartment != null }.map { top ->
    TopicPresenter(
      top,
      DepartmentPresenter(
        top.teachingDepartment!!,
        warwickDepartmentsService.findByDepartmentCode(top.teachingDepartment.code)
      )
    )
  }.sortedWith(compareBy(TopicPresenter::weighting).reversed().thenBy { it.department.name })

  private fun presentAvailablity(items: Collection<ModuleAvailability>) = items
    .groupBy { it.courseCode }.values
    .map(::CourseAvailabilityPresenter)
    .sortedWith(compareBy(CourseAvailabilityPresenter::courseName).thenBy(CourseAvailabilityPresenter::courseCode))

  val coreAvailability = presentAvailablity(availability.filter { it.selectionStatus != ModuleSelectionStatus.Optional })
  val optionalAvailability = presentAvailablity(availability.filter { it.selectionStatus == ModuleSelectionStatus.Optional })
}

class TopicPresenter(topic: Topic, val department: DepartmentPresenter) {
  val weighting = topic.percentage
}

class StudyLocation(description: ModuleDescription) {
  val name = description.description ?: description.title ?: "Other"
  val primary = description.udf5 == true
}

class AssessmentGroupPresenter(pattern: AssessmentPattern, components: Collection<AssessmentComponent>) {
  val name = components.first().assessmentGroup!!

  val description = if (name == "AO") "Audit only" else when (name.first()) {
    'A' -> "Assessed"
    'B' -> "Examined"
    'C', 'D' -> "Assessed and examined"
    'V' -> "Visiting students"
    else -> "Other"
  }

  val default = pattern.defaultAssessmentGroup == name
  val components = components.sortedBy { it.key.sequence }.map(::AssessmentComponentPresenter)
}

class AssessmentComponentPresenter(component: AssessmentComponent) {
  val name = component.name
  val type = component.type?.name
  val weighting = component.weighting
  val description = component.description?.text
}

class DepartmentPresenter(department: Department, warwickDepartment: uk.ac.warwick.camcat.services.Department?) {
  val code = department.code
  val name = warwickDepartment?.name ?: department.name ?: department.code
  val shortName = warwickDepartment?.shortName ?: department.name ?: department.code
  val veryShortName = warwickDepartment?.veryShortName ?: department.name ?: department.code
}

class ModuleCost(mds: ModuleDescription) {
  val category = mds.title ?: "Other"
  val description = mds.description
  val costToStudent = mds.udf1?.let(::BigDecimal)
  val fundedBy = mds.udf2
}

interface StudyAmount {
  val type: String
  val requiredDescription: String?
  val optionalDescription: String?
  val zero: Boolean
}

class DurationStudyAmount(override val type: String, private val duration: Duration) : StudyAmount {
  override val requiredDescription = if (!zero) DurationFormatter.format(duration) else null

  override val optionalDescription = null

  override val zero: Boolean
    @JsonIgnore
    get() = duration.isZero
}

class SessionStudyAmount(override val type: String, mds: ModuleDescription) : StudyAmount {
  private fun duration(value: String?) =
    value?.let { Duration.ofMinutes((BigDecimal(it) * BigDecimal(60)).toLong()) } ?: Duration.ZERO

  private fun describe(sessions: Int, duration: Duration) =
    if (sessions == 0) null else "$sessions session${if (sessions != 1) "s" else ""} of ${DurationFormatter.format(
      duration
    )}"

  val requiredSessions = mds.udf1?.toInt() ?: 0
  val requiredSessionDuration: Duration = duration(mds.udf2)
  override val requiredDescription = describe(requiredSessions, requiredSessionDuration)

  val optionalSessions = mds.udf3?.toInt() ?: 0
  val optionalSessionDuration: Duration = duration(mds.udf4)
  override val optionalDescription = describe(optionalSessions, optionalSessionDuration)

  override val zero: Boolean
    @JsonIgnore
    get() = requiredSessions == 0 && requiredSessionDuration.isZero && optionalSessions == 0 && optionalSessionDuration.isZero
}

class AssociatedModulePresenter(module: Module) {
  val code = module.code
  val title = module.title
}

class CourseAvailabilityPresenter(availabilities: Collection<ModuleAvailability>) {
  val courseCode = availabilities.first().courseCode
  val courseName = availabilities.first().courseName
  val routes = availabilities.map(::AvailabilityPresenter)
    .sortedWith(compareBy(AvailabilityPresenter::optionalCore).thenBy(AvailabilityPresenter::block).thenBy(AvailabilityPresenter::routeName).thenBy(AvailabilityPresenter::routeCode))
}

class AvailabilityPresenter(availability: ModuleAvailability) {
  val routeCode = availability.routeCode
  val routeName = availability.routeName
  val type = availability.selectionStatus?.name

  @JsonIgnore
  val core = availability.selectionStatus == ModuleSelectionStatus.Compulsory
  @JsonIgnore
  val optionalCore = availability.selectionStatus == ModuleSelectionStatus.OptionalCore
  @JsonIgnore
  val optional = availability.selectionStatus == ModuleSelectionStatus.Optional

  val block = if (optionalCore) null else availability.block
  val year = if (optionalCore) null else availability.blockYear
}

