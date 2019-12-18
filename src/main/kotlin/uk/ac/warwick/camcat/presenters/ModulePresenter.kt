package uk.ac.warwick.camcat.presenters

import com.fasterxml.jackson.annotation.JsonIgnore
import humanize.Humanize.pluralize
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import uk.ac.warwick.camcat.helpers.DurationFormatter
import uk.ac.warwick.camcat.services.*
import uk.ac.warwick.camcat.services.AssessmentType
import uk.ac.warwick.camcat.sits.entities.*
import uk.ac.warwick.camcat.sits.repositories.ModuleAvailability
import uk.ac.warwick.camcat.sits.services.ModuleService
import uk.ac.warwick.camcat.sits.services.RelatedModules
import uk.ac.warwick.camcat.system.cache.VariableTtlCacheDecorator
import uk.ac.warwick.util.termdates.AcademicYear
import java.math.BigDecimal
import java.time.Duration

@Component
class ModulePresenterFactory(
  private val userPresenterFactory: UserPresenterFactory,
  private val departmentService: DepartmentService,
  private val moduleService: ModuleService,
  private val assessmentTypeService: ModuleApprovalAssessmentTypeService,
  @Value("#{variableTtlCacheFactory.getCache('module')}") private val cache: VariableTtlCacheDecorator
) {
  fun build(moduleCode: String, academicYear: AcademicYear): ModulePresenter? = cache.get("$moduleCode,$academicYear") {
    moduleService.findByModuleCode(moduleCode)?.let { module ->
      val occurrences = moduleService.findOccurrences(module.code, academicYear)

      if (occurrences.isEmpty()) {
        null
      } else build(
        module = module,
        occurrenceCollection = occurrences,
        descriptions = moduleService.findDescriptions(module.code, academicYear),
        relatedModules = moduleService.findRelatedModules(module.code, academicYear),
        topics = moduleService.findTopics(module.code, academicYear),
        availability = moduleService.findAvailability(module.code, academicYear),
        assessmentComponents = moduleService.findAssessmentComponents(module.code, academicYear)
      )
    }
  }

  fun build(
    module: Module,
    occurrenceCollection: Collection<ModuleOccurrence>,
    descriptions: Collection<ModuleDescription>,
    relatedModules: RelatedModules,
    topics: Collection<Topic>,
    availability: Collection<ModuleAvailability>,
    assessmentComponents: Collection<AssessmentComponent>
  ): ModulePresenter {
    val sortedOccurrences = occurrenceCollection.sortedBy { it.key.occurrenceCode }
    val primaryOccurrence =
      sortedOccurrences.find { it.key.occurrenceCode == "A" } ?: sortedOccurrences.firstOrNull()

    val descriptionsByCode = descriptions.groupBy { it.code }.mapValues { it.value.sortedBy { it.key.sequence } }

    fun descriptions(code: String): List<ModuleDescription> = descriptionsByCode[code].orEmpty()
    fun description(code: String): ModuleDescription? = descriptions(code).firstOrNull()
    fun descriptionText(code: String): String? = description(code)?.description

    val code = module.code
    val stemCode = module.code.take(5)
    val title = module.title ?: "Untitled module"
    val creditValue = module.creditValue ?: module.code.takeLastWhile { it != '-' }.let(::BigDecimal)

    val department = module.departmentCode?.let(departmentService::findByDepartmentCode)
    val faculty = department?.faculty

    val level = primaryOccurrence?.level
    val leader = primaryOccurrence?.moduleLeaderPersonnelCode?.let(userPresenterFactory::buildFromPersonnelCode)

    val duration = primaryOccurrence?.moduleDuration?.let(DurationFormatter::durationInDaysOrWeeks)

    val locations = descriptions("MA010").map(StudyLocation.Companion::build)
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
      description("MA013")?.let { SessionStudyAmount.build("Lectures", it) },
      description("MA014")?.let { SessionStudyAmount.build("Seminars", it) },
      description("MA015")?.let { SessionStudyAmount.build("Tutorials", it) },
      description("MA016")?.let { SessionStudyAmount.build("Project supervision", it) },
      description("MA017")?.let { SessionStudyAmount.build("Demonstrations", it) },
      description("MA018")?.let { SessionStudyAmount.build("Practical classes", it) },
      description("MA019")?.let { SessionStudyAmount.build("Supervised practical classes", it) },
      description("MA020")?.let { SessionStudyAmount.build("Fieldwork", it) },
      description("MA021")?.let { SessionStudyAmount.build("External visits", it) },
      description("MA022")?.let { SessionStudyAmount.build("Work-based learning", it) },
      description("MA026")?.title?.let {
        DurationStudyAmount.build(
          "Private study",
          Duration.ofHours(BigDecimal(it).longValueExact())
        )
      }
    ).filterNot { it.zero }

    val totalStudyHours =
      BigDecimal(studyAmounts.sumBy { it.requiredDuration.toMinutes().toInt() }).divide(BigDecimal(60))

    val preRequisiteModules = relatedModules.preRequisites.map(AssociatedModulePresenter.Companion::build)
    val postRequisiteModules = relatedModules.postRequisites.map(AssociatedModulePresenter.Companion::build)
    val antiRequisiteModules = relatedModules.antiRequisites.map(AssociatedModulePresenter.Companion::build)

    val assessmentGroups = listOf(AssessmentGroupPresenter.build(assessmentComponents, assessmentTypeService))

    val costs = descriptions("MA031").map(ModuleCost.Companion::build)

    val hasAuditOnlyAssessmentGroup = module.assessmentPattern?.components?.any { it.assessmentGroup == "AO" } == true

    val teachingSplits = topics.filter { it.teachingDepartmentCode != null }.mapNotNull { top ->
      val department = departmentService.findByDepartmentCode(top.teachingDepartmentCode!!)


      if (department != null) TopicPresenter.build(top, department)
      else null
    }.sortedWith(compareBy(TopicPresenter::weighting).reversed().thenBy { it.department.name })

    fun presentAvailablity(items: Collection<ModuleAvailability>) = items
      .groupBy { it.courseCode }.values
      .map(CourseAvailabilityPresenter.Companion::build)
      .sortedWith(compareBy(CourseAvailabilityPresenter::courseName).thenBy(CourseAvailabilityPresenter::courseCode))

    val coreAvailability =
      presentAvailablity(availability.filter { it.selectionStatus != ModuleSelectionStatus.Optional })
    val optionalAvailability =
      presentAvailablity(availability.filter { it.selectionStatus == ModuleSelectionStatus.Optional })

    return ModulePresenter(
      code = code,
      stemCode = stemCode,
      title = title,
      creditValue = creditValue,
      department = department,
      faculty = faculty,
      level = level,
      leader = leader,
      duration = duration,
      locations = locations,
      aims = aims,
      assessmentFeedback = assessmentFeedback,
      indicativeReadingList = indicativeReadingList,
      interdisciplinary = interdisciplinary,
      international = international,
      introductoryDescription = introductoryDescription,
      learningOutcomes = learningOutcomes,
      mustPassAllAssessmentComponents = mustPassAllAssessmentComponents,
      otherActivityDescription = otherActivityDescription,
      outlineSyllabus = outlineSyllabus,
      privateStudyDescription = privateStudyDescription,
      readingListUrl = readingListUrl,
      researchElement = researchElement,
      subjectSpecificSkills = subjectSpecificSkills,
      transferableSkills = transferableSkills,
      url = url,
      studyAmounts = studyAmounts,
      totalStudyHours = totalStudyHours,
      preRequisiteModules = preRequisiteModules,
      postRequisiteModules = postRequisiteModules,
      antiRequisiteModules = antiRequisiteModules,
      assessmentGroups = assessmentGroups,
      costs = costs,
      hasAuditOnlyAssessmentGroup = hasAuditOnlyAssessmentGroup,
      teachingSplits = teachingSplits,
      coreAvailability = coreAvailability,
      optionalAvailability = optionalAvailability
    )
  }
}

data class ModulePresenter(
  val code: String,
  val stemCode: String,
  val title: String,
  val creditValue: BigDecimal,
  val department: Department?,
  val faculty: Faculty?,
  val level: Level?,
  val leader: UserPresenter?,
  val duration: String?,
  val locations: List<StudyLocation>,
  val aims: String?,
  val assessmentFeedback: String?,
  val indicativeReadingList: String?,
  val interdisciplinary: String?,
  val international: String?,
  val introductoryDescription: String?,
  val learningOutcomes: List<String>,
  val mustPassAllAssessmentComponents: Boolean,
  val otherActivityDescription: String?,
  val outlineSyllabus: String?,
  val privateStudyDescription: String?,
  val readingListUrl: String?,
  val researchElement: String?,
  val subjectSpecificSkills: String?,
  val transferableSkills: String?,
  val url: String?,
  val studyAmounts: List<StudyAmount>,
  val totalStudyHours: BigDecimal,
  val preRequisiteModules: List<AssociatedModulePresenter>,
  val postRequisiteModules: List<AssociatedModulePresenter>,
  val antiRequisiteModules: List<AssociatedModulePresenter>,
  val assessmentGroups: List<AssessmentGroupPresenter>,
  val costs: List<ModuleCost>,
  val hasAuditOnlyAssessmentGroup: Boolean,
  val teachingSplits: List<TopicPresenter>,
  val coreAvailability: List<CourseAvailabilityPresenter>,
  val optionalAvailability: List<CourseAvailabilityPresenter>
)

data class TopicPresenter(
  val department: Department,
  val weighting: Int?
) {
  companion object {
    fun build(topic: Topic, department: Department): TopicPresenter = TopicPresenter(
      department,
      topic.percentage
    )
  }
}

data class StudyLocation(
  val name: String,
  val primary: Boolean
) {
  companion object {
    fun build(description: ModuleDescription) = StudyLocation(
      name = description.description ?: description.title ?: "Other",
      primary = description.udf5 == true
    )
  }
}

data class AssessmentGroupPresenter(
  val components: Collection<AssessmentComponentPresenter>
) {
  companion object {
    fun build(components: Collection<AssessmentComponent>, assessmentTypeService: ModuleApprovalAssessmentTypeService) =
      AssessmentGroupPresenter(
        components = components
          .sortedBy { it.key.sequence }
          .map { component ->
            AssessmentComponentPresenter.build(
              component,
              component.type?.let { ast -> assessmentTypeService.findByCode(ast.code) })
          }
      )
  }
}

data class AssessmentComponentPresenter(
  val name: String?,
  @JsonIgnore val typeCode: String?,
  val type: String?,
  val weighting: Int,
  val description: String?,
  val length: String?,
  val studyTime: String?
) {
  companion object {
    fun build(component: AssessmentComponent, type: AssessmentType?) =
      AssessmentComponentPresenter(
        name = component.name,
        typeCode = component.type?.code,
        type = component.type?.name,
        weighting = component.weighting ?: 0,
        description = component.description?.text.takeUnless { it == component.name },
        length = component.length?.let { length ->
          when (type?.lengthType) {
            AssessmentLengthType.ExamDuration, AssessmentLengthType.Duration ->
              DurationFormatter.format(Duration.ofMinutes(length))
            AssessmentLengthType.WordCount ->
              pluralize("1 word", "{0} words", "Unspecified word count", length)
            else ->
              component.duration?.let(DurationFormatter::format)
          }
        },
        studyTime = component.studyHours?.let { hours ->
          DurationFormatter.format(Duration.ofMinutes(hours.multiply(BigDecimal(60)).toLong()))
        }
      )
  }
}


data class ModuleCost(
  val category: String,
  val description: String?,
  val costToStudent: BigDecimal?,
  val fundedBy: String?
) {
  companion object {
    fun build(mds: ModuleDescription): ModuleCost = ModuleCost(
      category = mds.title ?: "Other",
      description = mds.description,
      costToStudent = mds.udf1?.let(::BigDecimal),
      fundedBy = mds.udf2
    )
  }
}

data class StudyAmount(
  val type: String,
  val requiredDescription: String?,
  val optionalDescription: String?,
  val zero: Boolean,
  val requiredDuration: Duration
)

object DurationStudyAmount {
  fun build(type: String, duration: Duration): StudyAmount = StudyAmount(
    type = type,
    requiredDescription = if (!duration.isZero) DurationFormatter.format(duration) else null,
    requiredDuration = duration,
    optionalDescription = null,
    zero = duration.isZero
  )
}

object SessionStudyAmount {
  fun build(type: String, mds: ModuleDescription): StudyAmount {
    fun duration(value: String?) =
      value?.let { Duration.ofMinutes((BigDecimal(it) * BigDecimal(60)).toLong()) } ?: Duration.ZERO

    fun describe(sessions: Int, duration: Duration) =
      if (sessions == 0) null else pluralize("1 session of ", "{0} sessions of ", "", sessions) +DurationFormatter.format(
        duration
      )

    val requiredSessions = mds.udf1?.toInt() ?: 0
    val requiredSessionDuration: Duration = duration(mds.udf2)
    val requiredDescription = describe(requiredSessions, requiredSessionDuration)
    val requiredDuration = requiredSessionDuration.multipliedBy(requiredSessions.toLong())

    val optionalSessions = mds.udf3?.toInt() ?: 0
    val optionalSessionDuration: Duration = duration(mds.udf4)
    val optionalDescription = describe(optionalSessions, optionalSessionDuration)


    return StudyAmount(
      type = type,
      requiredDescription = requiredDescription,
      optionalDescription = optionalDescription,
      zero = requiredSessions == 0 && requiredSessionDuration.isZero && optionalSessions == 0 && optionalSessionDuration.isZero,
      requiredDuration = requiredDuration
    )
  }
}

data class AssociatedModulePresenter(
  val code: String,
  val title: String?
) {
  companion object {
    fun build(module: Module): AssociatedModulePresenter = AssociatedModulePresenter(
      code = module.code,
      title = module.title
    )
  }
}

data class CourseAvailabilityPresenter(
  val courseCode: String,
  val courseName: String?,
  val routes: List<AvailabilityPresenter>
) {
  companion object {
    fun build(availabilities: Collection<ModuleAvailability>): CourseAvailabilityPresenter =
      CourseAvailabilityPresenter(
        courseCode = availabilities.first().courseCode,
        courseName = availabilities.first().courseName,
        routes = availabilities.map(AvailabilityPresenter.Companion::build)
          .sortedWith(
            compareBy(AvailabilityPresenter::optionalCore).thenBy(AvailabilityPresenter::block).thenBy(
              AvailabilityPresenter::routeName
            ).thenBy(AvailabilityPresenter::routeCode)
          )
      )
  }
}

data class AvailabilityPresenter(
  val routeCode: String,
  val routeName: String?,
  val type: String?,
  @JsonIgnore val core: Boolean,
  @JsonIgnore val optionalCore: Boolean,
  @JsonIgnore val optional: Boolean,
  val block: String?,
  val year: Int?
) {
  companion object {
    fun build(availability: ModuleAvailability): AvailabilityPresenter {
      val optionalCore = availability.selectionStatus == ModuleSelectionStatus.OptionalCore

      return AvailabilityPresenter(
        routeCode = availability.routeCode,
        routeName = availability.routeName,
        type = availability.selectionStatus?.name,

        core = availability.selectionStatus == ModuleSelectionStatus.Compulsory,
        optionalCore = optionalCore,
        optional = availability.selectionStatus == ModuleSelectionStatus.Optional,

        block = if (optionalCore) null else availability.block,
        year = if (optionalCore) null else availability.blockYear
      )
    }
  }
}

