package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
import org.hibernate.annotations.Type
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Immutable
@Table(schema = "INTUIT", name = "INS_ESB")
data class ExternalSubject(
  @Id
  @Column(name = "ESB_CODE")
  val code: String,

  @Column(name = "ESB_SNAM")
  val shortName: String,

  @Column(name = "ESB_NAME")
  val name: String?,

  @Column(name = "ESB_DESC")
  val description: String?,

  @Column(name = "ESB_IUSE")
  @Type(type = "yes_no")
  val inUse: Boolean?
)
