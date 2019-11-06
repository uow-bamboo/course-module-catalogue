package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction
import java.io.Serializable
import javax.persistence.*

@Entity
@Immutable
@Table(schema = "INTUIT", name = "CAM_FME")
data class FormedModuleCollectionElement(
  @EmbeddedId
  val key: FormedModuleCollectionElementKey,

  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "LEV_CODE", referencedColumnName = "LEV_CODE")
  val level: Level?,

  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "SCH_CODE", referencedColumnName = "SCH_CODE")
  val scheme: Scheme?,

  @Column(name = "MTC_CODE")
  val moduleTypeCode: String?,

  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "FME_MODP", referencedColumnName = "MOD_CODE")
  val module: Module?
)

@Embeddable
data class FormedModuleCollectionElementKey(
  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "FMC_CODE", referencedColumnName = "FMC_CODE")
  val formedModuleCollection: FormedModuleCollection,

  @Column(name = "FME_SEQ")
  val sequence: String
) : Serializable
