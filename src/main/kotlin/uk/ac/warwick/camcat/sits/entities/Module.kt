package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
import org.hibernate.annotations.Where
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Immutable
@Table(schema = "INTUIT", name = "INS_MOD")
@Where(clause = "MOD_IUSE = 'Y'")
data class Module(
  @Id
  @Column(name = "MOD_CODE")
  val code: String,

  @Column(name = "MOD_NAME")
  val name: String?,

  @Column(name = "MOD_IUSE")
  val inUse: Boolean?
)
