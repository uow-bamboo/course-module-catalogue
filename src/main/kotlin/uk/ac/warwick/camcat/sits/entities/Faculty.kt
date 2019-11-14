package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
import org.hibernate.annotations.Type
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Immutable
@Table(name = "SRS_FAC")
data class Faculty(
  @Id
  @Column(name = "FAC_CODE")
  val code: String,

  @Column(name = "FAC_NAME")
  val name: String?,

  @Column(name = "FAC_IUSE")
  @Type(type = "yes_no")
  val inUse: Boolean?
) : Serializable
