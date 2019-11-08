package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.*
import java.math.BigDecimal
import javax.persistence.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Immutable
@Table(name = "INS_MOD")
@Where(clause = "MOD_CODE LIKE '%-%'")
data class Module(
  @Id
  @Column(name = "MOD_CODE")
  val code: String,

  @Column(name = "MOD_NAME")
  val title: String?,

  @JoinColumn(name = "DPT_CODE")
  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  val department: Department?,

  @JoinColumn(name = "MOD_CODE")
  @OneToMany(fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  val topics: Collection<Topic>,

  @JoinColumn(name = "MAP_CODE")
  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  val assessmentPattern: AssessmentPattern?,

  @Column(name = "MOD_CRDT")
  val creditValue: BigDecimal?
)
