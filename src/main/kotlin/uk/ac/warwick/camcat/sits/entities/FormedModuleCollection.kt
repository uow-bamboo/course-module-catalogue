package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.*
import java.io.Serializable
import javax.persistence.*
import javax.persistence.Entity
import javax.persistence.Table

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

  @OneToMany
  @Fetch(FetchMode.SELECT)
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "PDM_FMCC", referencedColumnName = "FMC_CODE")
  val pathwayDietModules: Collection<PathwayDietModule>,

  @Column(name = "FMC_SNAM")
  val shortName: String?,

  @OneToMany
  @Fetch(FetchMode.SELECT)
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "FMC_CODE", referencedColumnName = "FMC_CODE")
  val formedModuleCollectionElements: Collection<FormedModuleCollectionElement>?,

  @OneToMany
  @Fetch(FetchMode.SELECT)
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "FMC_CODE")
  val moduleRuleElements: Collection<ModuleRuleBody>
) : Serializable
