package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction
import java.io.Serializable
import javax.persistence.*

@Entity
@Immutable
@Table(schema = "INTUIT", name = "CAM_ADR")
data class AssessmentPaperDivisionRequirement(
  @EmbeddedId
  val key: AssessmentPaperDivisionRequirementKey,

  @JoinColumn(name = "ADR_ARQC", referencedColumnName = "ARQ_CODE")
  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  val requirement: AssessmentPaperRequirement?,

  @Column(name = "ADR_NUMO")
  val quantity: Int?
)

@Embeddable
data class AssessmentPaperDivisionRequirementKey(
  @Column(name = "ADR_APAC")
  val assessmentPaperCode: String,

  @Column(name = "ADR_ADVC")
  val assessmentPaperDivisionCode: String,

  @Column(name = "ADR_SEQN")
  val sequence: String
) : Serializable
