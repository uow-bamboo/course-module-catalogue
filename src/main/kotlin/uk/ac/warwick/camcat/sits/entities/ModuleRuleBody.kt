package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction
import java.io.Serializable
import javax.persistence.*

@Entity
@Immutable
@Table(name = "CAM_MMB")
data class ModuleRuleBody(
  @EmbeddedId
  val key: ModuleRuleBodyKey,

  // possible values are - T (taking), N (Not Failed), P (passed) or Y (taking in same year)
  @Column(name = "MMB_TFLAG")
  val ruleTakingFlag: String?,

  @Column(name = "MMB_MIN")
  val min: Int,

  @Column(name = "MMB_MAX")
  val max: Int,

  // possible values are -  M (module), C (credits), A (average), B (average best), P(Progression Rule ID), S (Student Qualification and Experience)
  @Column(name = "MMB_UCRD")
  val selectionData: String?,

  @JoinColumn(name = "FMC_CODE")
  @NotFound(action = NotFoundAction.IGNORE)
  @OneToOne
  val formedModuleCollection: FormedModuleCollection?,

  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumns(
    JoinColumn(name = "MOD_CODE", referencedColumnName = "MOD_CODE", insertable = false, updatable = false),
    JoinColumn(name = "MMR_CODE", referencedColumnName = "MMR_CODE", insertable = false, updatable = false)
  )
  val rule: ModuleRule?
)

@Embeddable
data class ModuleRuleBodyKey(
  @Column(name = "MOD_CODE")
  val moduleCode: String,

  @Column(name = "MMR_CODE")
  val moduleRuleCode: String,

  @Column(name = "MMB_SEQ")
  val sequence: String
) : Serializable
