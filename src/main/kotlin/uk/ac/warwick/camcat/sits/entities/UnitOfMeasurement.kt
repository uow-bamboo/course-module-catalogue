package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Immutable
@Table(schema = "INTUIT", name = "SRS_UOM")
data class UnitOfMeasurement(
  @Id
  @Column(name = "UOM_CODE")
  val code: String,

  @Column(name = "UOM_NAME")
  val name: String?
)
