package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Immutable
@Table(schema = "INTUIT", name = "SRS_FAC")
data class Faculty(
  @Id
  @Column(name = "FAC_CODE")
  val code: String,

  @Column(name = "FAC_NAME")
  val name: String?
)
