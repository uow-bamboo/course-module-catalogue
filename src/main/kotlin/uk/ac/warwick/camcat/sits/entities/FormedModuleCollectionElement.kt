package uk.ac.warwick.camcat.sits.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.*
import java.io.Serializable
import javax.persistence.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Immutable
@Table(name = "CAM_FME")
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

  @ManyToOne(fetch = FetchType.EAGER)
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "FME_MODP", referencedColumnName = "MOD_CODE")
  val module: Module?
)

@Embeddable
data class FormedModuleCollectionElementKey(
  @ManyToOne(fetch = FetchType.EAGER)
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "FMC_CODE", referencedColumnName = "FMC_CODE")
  @JsonIgnore
  val formedModuleCollection: FormedModuleCollection,

  @Column(name = "FME_SEQ")
  val sequence: String
) : Serializable {
  val formedModuleCollectionCode: String
    get() = formedModuleCollection.code
}
