package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.*
import javax.persistence.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Immutable
@Table(name = "SRS_MCR")
data class MasCourse(
  @Id
  @Column(name = "MCR_CODE")
  val mcrCode: String,

  @Column(name = "MCR_NAME")
  val name: String?,

  @Column(name = "MCR_TITL")
  val title: String?,

  /**
   * A code which defines the first block and the first block occurrence
   * of the block onto which a student would normally be admitted if the
   * block is to run more than once during the same academic year.
   * This field is validated by an entry in the Course Block Occurrence
   * (CBO) table of the SRS course defined above (6 chars)
   */
  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumns(
    JoinColumn(referencedColumnName = "CBO_OCCL", name = "MCR_OCCL"),
    JoinColumn(referencedColumnName = "CBO_BLOK", name = "MCR_BLOK"),
    JoinColumn(referencedColumnName = "CBO_AYRC", name = "MCR_DAYR"),
    JoinColumn(referencedColumnName = "CBO_CRSC", name = "MCR_CRSC")
  )
  val courseBlockOccurrence: CourseBlockOccurrence,

  @OneToMany(fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  @JoinColumn(referencedColumnName = "MCR_CODE", name = "VAR_MCRC")
  val validAdmissionRoutes: Collection<ValidAdmissionRoute>?,

  @Column(name = "MCR_AESC")
  val admissionEntrySystemCode: String,

  @ManyToOne
  @JoinColumn(name = "MCR_MOAC")
  val modeOfAttendance: ModeOfAttendance,

  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "MCR_SCHC")
  val scheme: Scheme,

  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "MCR_FACC")
  val faculty: Faculty?,

  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "MCR_DPTC")
  val department: Department?,

  @Column(name = "MCR_PRS1")
  val firstTutorCode: String,

  @Column(name = "MCR_IUSE")
  @Type(type = "yes_no")
  val inUse: Boolean?

)
