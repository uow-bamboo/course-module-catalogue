package uk.ac.warwick.camcat.sits.entities

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.hibernate.annotations.Immutable
import org.hibernate.annotations.Type
import uk.ac.warwick.camcat.system.serializers.DurationSerializer
import java.io.Serializable
import java.time.Duration
import javax.persistence.*

@Entity
@Immutable
@Table(schema = "INTUIT", name = "CAM_ADV")
data class AssessmentPaperDivision(
  @EmbeddedId
  val key: AssessmentPaperDivisionKey,

  @Column(name = "ADV_DURA")
  @Type(type = "uk.ac.warwick.camcat.sits.types.DurationType")
  @JsonSerialize(using = DurationSerializer::class)
  val duration: Duration?,

  @Column(name = "ADV_RDTM")
  @Type(type = "uk.ac.warwick.camcat.sits.types.DurationType")
  @JsonSerialize(using = DurationSerializer::class)
  val readingTime: Duration?,

  @OneToMany(fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  @JoinColumns(
    JoinColumn(name = "ADR_APAC", referencedColumnName = "ADV_APAC"),
    JoinColumn(name = "ADR_ADVC", referencedColumnName = "ADV_CODE")
  )
  val requirements: Collection<AssessmentPaperDivisionRequirement>
)

@Embeddable
data class AssessmentPaperDivisionKey(
  @Column(name = "ADV_APAC")
  val assessmentPaperCode: String,

  @Column(name = "ADV_CODE")
  val code: String
) : Serializable
