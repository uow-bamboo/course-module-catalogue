package uk.ac.warwick.camcat.sits.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.*
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

  @Column(name = "FMC_SNAM")
  val shortName: String?,

  @OneToMany(fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  @JoinColumn(name = "FMC_CODE", referencedColumnName = "FMC_CODE")
  val formedModuleCollectionElements: Collection<FormedModuleCollectionElement>?,

  @OneToMany
  @Fetch(FetchMode.SELECT)
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "FMC_CODE")
  @JsonIgnore
  val moduleRuleElements: Collection<ModuleRuleBody>
)
