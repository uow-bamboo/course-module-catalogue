package uk.ac.warwick.camcat.sits.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.*
import java.time.LocalDate
import javax.persistence.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Immutable
@Table(schema = "INTUIT", name = "SRS_CRS")
@Where(clause = "CRS_IUSE = 'Y'")
data class Course(
  @Id
  @Column(name = "CRS_CODE")
  val code: String,

  @Column(name = "CRS_TITL")
  val title: String?,

  @Column(name = "CRS_IUSE")
  val inUse: Boolean?,

  @Column(name = "CRS_BEGD")
  val startDate: LocalDate?,

  @Column(name = "CRS_NAME")
  val name: String?,

  @Column(name = "CRS_SNAM")
  val shortName: String?,

  @ManyToOne
  @JoinColumn(referencedColumnName = "MOA_CODE", name = "CRS_MOAC")
  @NotFound(action = NotFoundAction.IGNORE)
  val modeOfAttendance: ModeOfAttendance?,

  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(referencedColumnName = "SCH_CODE", name = "CRS_SCHC")
  val scheme: Scheme?,

  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(referencedColumnName = "FAC_CODE", name = "CRS_FACC")
  val faculty: Faculty?,

  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(referencedColumnName = "DPT_CODE", name = "CRS_DPTC")
  val department: Department?,

  @Column(name = "CRS_CGPC")
  val courseGroupCode: String?,

  @Column(name = "CRS_PRSC")
  val responsiblePersonnelCode: String?,

  @Column(name = "CRS_TTIC")
  val teacherTrainerIdentifierCode: String?,

  @Column(name = "CRS_BURS")
  val eligibleForNhsBursary: Boolean?,

  @Column(name = "CRS_RESH")
  val isResearchCourse: Boolean?,

  @Column(name = "CRS_CTYC")
  val courseTypeCode: String?,

  @Column(name = "CRS_QULC")
  val qualificationAimsCode: String?,

  @ManyToOne
  @JoinColumn(referencedColumnName = "ESB_CODE", name = "CRS_ESB1")
  @NotFound(action = NotFoundAction.IGNORE)
  @JsonIgnore
  val externalSubject1: ExternalSubject?,

  @ManyToOne
  @JoinColumn(referencedColumnName = "ESB_CODE", name = "CRS_ESB2")
  @NotFound(action = NotFoundAction.IGNORE)
  @JsonIgnore
  val externalSubject2: ExternalSubject?,

  @ManyToOne
  @JoinColumn(referencedColumnName = "ESB_CODE", name = "CRS_ESB3")
  @NotFound(action = NotFoundAction.IGNORE)
  @JsonIgnore
  val externalSubject3: ExternalSubject?,

  @Embedded
  val courseLength: CourseLength?,

  @Embedded
  val fundingSource: FundingSource?,

  @Column(name = "CRS_ERFM")
  val externalReturnFormat: String?,

  @Column(name = "CRS_XRFM")
  val extensionToReturnFormat: String?,

  @Column(name = "CRS_FECM")
  val feCourseMarker: String?,

  @OneToMany(fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  @JoinColumn(name = "MCR_CRSC", referencedColumnName = "CRS_CODE")
  val MasCourses: Collection<MasCourse>
) {
  val externalSubjects: List<ExternalSubject>
    get() = listOfNotNull(externalSubject1, externalSubject2, externalSubject3)
}

@Embeddable
data class FundingSource(
  @Column(name = "CRS_CSF1")
  val major: String?,

  @Column(name = "CRS_CLSC")
  val minor: String?
)

@Embeddable
data class CourseLength(
  @Column(name = "CRS_YLEN")
  val inYears: Int?,

  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(referencedColumnName = "UOM_CODE", name = "CRS_UOMC")
  val unitOfMeasure: UnitOfMeasurement?,

  @Column(name = "CRS_LENG")
  val quantity: Int?
)
