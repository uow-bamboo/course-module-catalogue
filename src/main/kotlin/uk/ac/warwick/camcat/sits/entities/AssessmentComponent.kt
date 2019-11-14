package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.*
import uk.ac.warwick.util.termdates.AcademicYear
import java.io.Serializable
import java.time.Duration
import javax.persistence.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Immutable
@Table(name = "CAM_MAB")
data class AssessmentComponent(
  @EmbeddedId
  val key: AssessmentComponentKey,

  @Column(name = "MAB_AGRP")
  val assessmentGroup: String?,

  @Column(name = "MAB_PERC")
  val weighting: Int?,

  @Column(name = "MAB_TSHA")
  val totalWeighting: Int?,

  @Column(name = "MAB_NAME")
  val name: String?,

  @JoinColumn(name = "AST_CODE")
  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  val type: AssessmentType?,

  @ManyToOne
  @JoinColumn(referencedColumnName = "APA_CODE", name = "MAB_APAC")
  @NotFound(action = NotFoundAction.IGNORE)
  val paper: AssessmentPaper?,

  @Column(name = "MAB_ADVC")
  val paperDivisionCode: String?,

  @Column(name = "MAB_FAYN")
  @Type(type = "yes_no")
  val final: Boolean?,

  @Column(name = "MAB_HOHM")
  @Type(type = "uk.ac.warwick.camcat.sits.types.DurationType")
  val duration: Duration?,

  @Column(name = "MAB_UDF1")
  @Type(type = "yes_no")
  val inUse: Boolean?,

  @Column(name = "MAB_UDF2")
  @Type(type = "yes_no")
  val canSelfCertify: Boolean?,

  @Formula("CASE WHEN MAB_UDF3 IS NOT NULL THEN 1 ELSE 0 END")
  @Type(type = "yes_no")
  val reassessment: Boolean,

  @Column(name = "MAB_UDF4")
  @Type(type = "uk.ac.warwick.camcat.sits.types.AcademicYearType")
  val introducedAcademicYear: AcademicYear?
) : Serializable

@Embeddable
data class AssessmentComponentKey(
  @Column(name = "MAP_CODE")
  val assessmentPatternCode: String,

  @Column(name = "MAB_SEQ")
  val sequence: String
) : Serializable
