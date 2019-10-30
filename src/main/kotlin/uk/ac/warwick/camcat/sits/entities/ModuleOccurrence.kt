package uk.ac.warwick.camcat.sits.entities

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.hibernate.annotations.Immutable
import org.hibernate.annotations.Type
import uk.ac.warwick.camcat.system.serializers.AcademicYearSerializer
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
  val department: Department?,

  @JoinColumn(name = "LEV_CODE")
  @ManyToOne
  val level: Level?,

  @JoinColumn(name = "LCA_CODE")
  @ManyToOne
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
  val occurrence: String,

  @Column(name = "AYR_CODE")
  @Type(type = "uk.ac.warwick.camcat.sits.types.AcademicYearType")
  @JsonSerialize(using = AcademicYearSerializer::class)
  val academicYear: AcademicYear,

  @Column(name = "PSL_CODE")
  val periodSlotCode: String
) : Serializable
