package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
import org.hibernate.annotations.Type
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
  val name: String?,

  @Column(name = "AST_PNAM")
  @Type(type = "yes_no")
  val printName: Boolean?,

  @Column(name = "AST_IUSE")
  @Type(type = "yes_no")
  val inUse: Boolean?
)
