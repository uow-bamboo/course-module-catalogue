package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction
import org.hibernate.annotations.Type
import java.io.Serializable
import javax.persistence.*

@Entity
@Immutable
@Table(name = "INS_DPT")
data class Department(
  @Id
  @Column(name = "DPT_CODE")
  val code: String,

  @Column(name = "DPT_NAME")
  val name: String?,

  @Column(name = "DPT_TYPE")
  val type: String?,

  @Column(name = "DPT_IUSE")
  @Type(type = "yes_no")
  val inUse: Boolean?,

  @JoinColumn(name = "DPT_FACC", referencedColumnName = "FAC_CODE")
  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  val faculty: Faculty?
) : Serializable
