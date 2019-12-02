package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.*
import javax.persistence.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Immutable
@Table(name = "INS_DPT")
@Where(clause = "DPT_IUSE = 'Y'")
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
  val faculty: Faculty?,

  @OneToMany(mappedBy = "department")
  val modules: Collection<Module>,

  @OneToMany(mappedBy = "department")
  val courses: Collection<Course>
)
