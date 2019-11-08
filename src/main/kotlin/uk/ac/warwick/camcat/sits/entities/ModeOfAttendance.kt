package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
import org.hibernate.annotations.Type
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Immutable
@Table(name = "INS_MOA")
data class ModeOfAttendance(
  @Id
  @Column(name = "MOA_CODE")
  val code: String,

  @Column(name = "MOA_NAME")
  val name: String?,

  @Column(name = "MOA_IUSE")
  @Type(type = "yes_no")
  val inUse: Boolean?
)
