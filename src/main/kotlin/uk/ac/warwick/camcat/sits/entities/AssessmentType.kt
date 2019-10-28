package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Immutable
@Table(schema = "INTUIT", name = "CAM_AST")
data class AssessmentType(
  @Id
  @Column(name = "AST_CODE")
  val code: String,

  @Column(name = "AST_NAME")
  val name: String?
)
