package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.hibernate.annotations.Immutable
import javax.persistence.*

@Entity
@Immutable
@Table(schema = "INTUIT", name = "INS_PRG")
data class Programme(
  @Id
  @Column(name = "PRG_CODE")
  val code: String,

  @Column(name = "PRG_NAME")
  val name: String?,

  @Column(name = "PRG_SNAM")
  val shortName: String?,

  @Column(name = "PRG_IUSE")
  val inUse: Boolean?,

  @Column(name = "PRG_PWYP")
  val requirePathwayProgression: Boolean?,

  @ManyToOne
  @JoinColumn(name = "PRG_SCHC", referencedColumnName = "SCH_CODE")
  val scheme: Scheme?,

  @Column(name = "PRG_MAWD")
  val allowMultipleAward: Boolean?
)
