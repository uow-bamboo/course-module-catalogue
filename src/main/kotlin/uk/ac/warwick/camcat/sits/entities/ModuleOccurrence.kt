package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction
import org.hibernate.annotations.Type
import uk.ac.warwick.util.termdates.AcademicYear
import java.io.Serializable
import javax.persistence.*

@Entity
@Immutable
@Table(schema = "INTUIT", name = "CAM_MAV")
data class ModuleOccurrence(
  @EmbeddedId
  val key: ModuleOccurrenceKey,

  @JoinColumn(name = "DPT_CODE")
  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  val department: Department?,

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

  @OneToOne
  @JoinColumns(
    JoinColumn(name = "MOD_CODE"),
    JoinColumn(name = "MAV_OCCUR"),
    JoinColumn(name = "AYR_CODE"),
    JoinColumn(name = "PSL_CODE")
  )
  val details: ModuleOccurrenceDetails?
)

@Embeddable
data class ModuleOccurrenceKey(
  @Column(name = "MOD_CODE")
  val moduleCode: String,

  @Column(name = "MAV_OCCUR")
  val occurrenceCode: String,

  @Column(name = "AYR_CODE")
  @Type(type = "uk.ac.warwick.camcat.sits.types.AcademicYearType")
  val academicYear: AcademicYear,

  @Column(name = "PSL_CODE")
  val periodSlotCode: String
) : Serializable
