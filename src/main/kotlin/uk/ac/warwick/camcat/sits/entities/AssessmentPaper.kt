package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.hibernate.annotations.Immutable
import javax.persistence.*

@Entity
@Immutable
@Table(schema = "INTUIT", name = "CAM_APA")
data class AssessmentPaper(
  @Id
  @Column(name = "APA_CODE")
  val code: String,

  @Column(name = "APA_NAME")
  val name: String?,

  @OneToMany(fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  @JoinColumn(name = "ADV_APAC", referencedColumnName = "APA_CODE")
  val divisions: Collection<AssessmentPaperDivision>
)
