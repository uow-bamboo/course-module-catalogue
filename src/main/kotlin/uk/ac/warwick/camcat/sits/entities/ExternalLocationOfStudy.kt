package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
import org.hibernate.annotations.Type
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Immutable
@Table(schema = "INTUIT", name = "SRS_ELS")
data class ExternalLocationOfStudy(
  @Id
  @Column(name = "ELS_CODE")
  val code: String,

  @Column(name = "ELS_NAME")
  val name: String?,

  @Column(name = "ELS_FEID")
  val feLocation: String?,

  @Column(name = "ELS_HLID")
  val hesaLocation: String?,

  @Column(name = "ELS_IUSE")
  @Type(type = "yes_no")
  val inUse: Boolean?
)
