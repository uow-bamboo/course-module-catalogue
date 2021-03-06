package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.*
import uk.ac.warwick.util.termdates.AcademicYear
import java.io.Serializable
import javax.persistence.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Immutable
@Table(name = "CAM_MAV")
@Where(clause = "AYR_CODE >= '20/21'")
data class ModuleOccurrence(
  @EmbeddedId
  val key: ModuleOccurrenceKey,

  @Column(name = "DPT_CODE")
  val departmentCode: String?,

  @JoinColumn(name = "LEV_CODE")
  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  val level: Level?,

  @JoinColumn(name = "LCA_CODE")
  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  val location: Location?,

  @Column(name = "MAV_MAVN")
  val name: String?,

  @Column(name = "PRS_CODE")
  val moduleLeaderPersonnelCode: String?,

  @Column(name = "MAV_UDF8")
  val moduleDuration: String?,

  @OneToOne
  @JoinColumns(
    JoinColumn(name = "MOD_CODE", referencedColumnName = "MOD_CODE", insertable = false, updatable = false),
    JoinColumn(name = "MAV_OCCUR", referencedColumnName = "MAV_OCCUR", insertable = false, updatable = false),
    JoinColumn(name = "AYR_CODE", referencedColumnName = "AYR_CODE", insertable = false, updatable = false),
    JoinColumn(name = "PSL_CODE", referencedColumnName = "PSL_CODE", insertable = false, updatable = false)
  )
  val details: ModuleOccurrenceDetails?
)

@Embeddable
data class ModuleOccurrenceKey(
  @ManyToOne
  @JoinColumn(name = "MOD_CODE", insertable = false, updatable = false)
  @NotFound(action = NotFoundAction.IGNORE)
  val module: Module,

  @Column(name = "MAV_OCCUR")
  val occurrenceCode: String,

  @Column(name = "AYR_CODE")
  @Type(type = "uk.ac.warwick.camcat.sits.types.AcademicYearType")
  val academicYear: AcademicYear,

  @Column(name = "PSL_CODE")
  val periodSlotCode: String
) : Serializable
