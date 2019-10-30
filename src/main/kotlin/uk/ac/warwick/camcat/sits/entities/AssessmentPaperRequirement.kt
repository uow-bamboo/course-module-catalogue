package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Immutable
@Table(schema = "INTUIT", name = "CAM_ARQ")
data class AssessmentPaperRequirement(
  @Id
  @Column(name = "ARQ_CODE")
  val code: String,

  @Column(name = "ARQ_NAME")
  val name: String?
)
