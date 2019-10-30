package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.hibernate.annotations.Immutable
import org.hibernate.annotations.Where
import javax.persistence.*

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

  @JoinColumn(name = "DPT_CODE")
  @ManyToOne
  val department: Department?,

  @JoinColumn(name = "MOD_CODE")
  @OneToMany(fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  val topics: Collection<Topic>,

  @JoinColumn(name = "MAP_CODE")
  @ManyToOne
  val assessmentPattern: AssessmentPattern?,

  @Column(name = "MOD_CRDT")
  val creditValue: Double?
)
