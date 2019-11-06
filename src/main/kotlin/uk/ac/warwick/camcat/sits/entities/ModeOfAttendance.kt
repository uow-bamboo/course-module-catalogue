package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Immutable
@Table(schema = "INTUIT", name = "INS_MOA")
data class ModeOfAttendance(
  @Id
  @Column(name = "MOA_CODE")
  val code: String,

  @Column(name = "MOA_NAME")
  val name: String?,

  @Column(name = "MOA_IUSE")
  val inUse: Boolean?
)
