package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
import javax.persistence.*

@Entity
@Immutable
@Table(schema = "INTUIT", name = "INS_DPT")
data class Department(
  @Id
  @Column(name = "DPT_CODE")
  val code: String,

  @Column(name = "DPT_NAME")
  val title: String?,

  @JoinColumn(name = "DPT_FACC", referencedColumnName = "FAC_CODE")
  @ManyToOne
  val faculty: Faculty?
)
