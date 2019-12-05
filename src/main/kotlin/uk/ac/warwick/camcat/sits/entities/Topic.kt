package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction
import org.hibernate.annotations.Type
import uk.ac.warwick.util.termdates.AcademicYear
import javax.persistence.*

@Entity
@Immutable
@Table(name = "CAM_TOP")
data class Topic(
  @Id
  @Column(name = "TOP_CODE")
  val code: String,

  @Column(name = "TOP_PERC")
  val percentage: Int?,

  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "MOD_CODE")
  val module: Module?,

  @Column(name = "DPT_CODE")
  val teachingDepartmentCode: String?,

  @Column(name = "TOP_UDF1")
  @Type(type = "uk.ac.warwick.camcat.sits.types.AcademicYearType")
  val academicYear: AcademicYear?
)
