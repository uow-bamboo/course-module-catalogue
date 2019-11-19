package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
import java.io.Serializable
import javax.persistence.*

@Entity
@Immutable
@Table(name = "CAM_MABD")
data class AssessmentComponentDescription(
  @EmbeddedId
  val key: AssessmentComponentDescriptionKey,

  @Column(name = "MAB_DESC")
  val text: String?
)

@Embeddable
data class AssessmentComponentDescriptionKey(
  @Column(name = "MAB_MAPC")
  val assessmentPatternCode: String,

  @Column(name = "MAB_MABS")
  val sequence: String
) : Serializable
