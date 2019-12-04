package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.*
import uk.ac.warwick.util.termdates.AcademicYear
import java.io.Serializable
import java.time.LocalDate
import javax.persistence.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Immutable
@Table(name = "SRS_CBO")
@Where(clause = "CBO_AYRC not like '0%/0%' and CBO_AYRC not like '0%/1%' and length(CBO_AYRC) = 5")
data class CourseBlockOccurrence(
  @EmbeddedId
  val key: CourseBlockOccurrenceKey,

  @ManyToOne
  @JoinColumn(name = "CBO_COHC")
  @NotFound(action = NotFoundAction.IGNORE)
  val cohort: Cohort?,

  @Column(name = "CBO_PSLC")
  val periodSlotCode: String?,

  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "CBO_LCAC")
  val location: Location?,

  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "CBO_ELSC")
  val externalStudyLocation: ExternalLocationOfStudy?,

  @Column(name = "CBO_FRN2")
  val franchisedOutCode: String?,

  @Column(name = "CBO_VALC")
  val validatingBodyCode: String?,

  @Column(name = "CBO_NAYR")
  @Type(type = "uk.ac.warwick.camcat.sits.types.AcademicYearType")
  val nextAcademicYear: AcademicYear?,

  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "CBO_NCRS", referencedColumnName = "CRS_CODE")
  val nextCourse: Course?,

  @ManyToOne
  @JoinColumn(name = "CBO_MOAC")
  @NotFound(action = NotFoundAction.IGNORE)
  val modeOfAttendance: ModeOfAttendance?,

  @Column(name = "CBO_CEPC")
  val courseEnrolmentProfileCode: String?,

  @Column(name = "CBO_BEGD")
  val startDate: LocalDate?,

  @Column(name = "CBO_ENDD")
  val endDate: LocalDate?,

  @Column(name = "CBO_NBLK")
  val nextBlock: String?,

  @Column(name = "CBO_NOCC")
  val nextOccurrenceLetter: String?,

  @Column(name = "CBO_EHCF")
  @Type(type = "yes_no")
  val eligibleForHefceCoreFunding: Boolean?,

  @Column(name = "CBO_YTYP")
  val hesaYearType: String?,

  @Column(name = "CBO_FNDL")
  val hesaFundingLevel: String?,

  @Column(name = "CBO_UFIP")
  val ufiPlace: String?,

  @Column(name = "CBO_LEFT")
  val placesLeft: Int?,

  @Column(name = "CBO_COLM")
  val conditionalOffersLeft: Int?,

  @Column(name = "CBO_UOLM")
  val unconditionalOffersLeft: Int?,

  @Column(name = "CBO_AOLM")
  val averageOffersLeft: Int?,

  @Column(name = "CBO_DELM")
  val directEnrolmentsLeft: Int?
)

@Embeddable
data class CourseBlockOccurrenceKey(
  @Column(name = "CBO_AYRC")
  @Type(type = "uk.ac.warwick.camcat.sits.types.AcademicYearType")
  val academicYear: AcademicYear,

  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumns(
    JoinColumn(name = "CBO_BLOK", referencedColumnName = "CBK_BLOK", insertable = false, updatable = false),
    JoinColumn(name = "CBO_CRSC", referencedColumnName = "CBK_CRSC", insertable = false, updatable = false)
  )
  val courseBlock: CourseBlock,

  /**
   * Course Block Occurrence (6 characters).
   * A code which defines the occurrence of the block.
   * If the block is to run more than once during the same academic year
   * the codes should define the sequence e.g. A,B,C. To avoid possible
   * confusion it is suggested that the course blocks are referred to
   * by number and occurrences by letter. Where only one occurrence of
   * a course block is offered the letter A should be used.
   */
  @Column(name = "CBO_OCCL")
  val occurrence: String
) : Serializable
