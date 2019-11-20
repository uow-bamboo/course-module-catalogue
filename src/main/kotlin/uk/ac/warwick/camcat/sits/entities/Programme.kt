package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.*
import javax.persistence.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Immutable
@Table(name = "INS_PRG")
data class Programme(
  @Id
  @Column(name = "PRG_CODE")
  val code: String,

  @Column(name = "PRG_NAME")
  val name: String?,

  @Column(name = "PRG_SNAM")
  val shortName: String?,

  @Column(name = "PRG_IUSE")
  @Type(type = "yes_no")
  val inUse: Boolean?,

  @Column(name = "PRG_PWYP")
  @Type(type = "yes_no")
  val requirePathwayProgression: Boolean?,

  @ManyToOne
  @JoinColumn(name = "PRG_SCHC", referencedColumnName = "SCH_CODE")
  val scheme: Scheme?,

  @Column(name = "PRG_MAWD")
  @Type(type = "yes_no")
  val allowMultipleAward: Boolean?,

  @ManyToMany(mappedBy = "programmes")
  @NotFound(action = NotFoundAction.IGNORE)
  @Fetch(FetchMode.SELECT)
  @LazyCollection(LazyCollectionOption.FALSE)
  val routes: Collection<Route>,

  @ManyToMany
  @NotFound(action = NotFoundAction.IGNORE)
  @Fetch(FetchMode.SELECT)
  @LazyCollection(LazyCollectionOption.FALSE)
  @JoinTable(
    name = "CAM_PAW",
    joinColumns = [
      JoinColumn(name = "PRG_CODE")
    ],
    inverseJoinColumns = [
      JoinColumn(name = "AWD_CODE")
    ]
  )
  val awards: Collection<Award>

)
