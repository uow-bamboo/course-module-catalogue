package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Immutable
@Table(schema = "INTUIT", name = "CAM_RCL")
data class RuleClass(
  @Id
  @Column(name = "RCL_CODE")
  val code: String,

  @Column(name = "RCL_SNAM")
  val name: String?
)
