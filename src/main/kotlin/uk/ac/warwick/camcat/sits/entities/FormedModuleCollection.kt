package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.hibernate.annotations.Immutable
import org.hibernate.annotations.Type
import javax.persistence.*

@Entity
@Immutable
@Table(name = "CAM_FMC")
data class FormedModuleCollection(
  @Id
  @Column(name = "FMC_CODE")
  val code: String,

  @Column(name = "FMC_IUSE")
  @Type(type = "yes_no")
  val inUse: Boolean?,

  @Column(name = "FMC_NAME")
  val name: String?,

  @Column(name = "FMC_SNAM")
  val shortName: String?,

  @OneToMany(fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  @JoinColumn(name = "FMC_CODE", referencedColumnName = "FMC_CODE")
  val formedModuleCollectionElements: Collection<FormedModuleCollectionElement>?
)
