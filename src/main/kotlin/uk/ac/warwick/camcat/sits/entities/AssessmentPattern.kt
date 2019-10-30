package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.hibernate.annotations.Immutable
import javax.persistence.*

@Entity
@Immutable
@Table(schema = "INTUIT", name = "CAM_MAP")
data class AssessmentPattern(
  @Id
  @Column(name = "MAP_CODE")
  val code: String,

  @Column(name = "MAP_AGRP")
  val defaultAssessmentGroup: String,

  @OneToMany(fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  @JoinColumn(name = "MAP_CODE")
  val components: Collection<AssessmentComponent>
)
