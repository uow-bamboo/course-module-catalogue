package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.*
import uk.ac.warwick.util.termdates.AcademicYear
import java.io.Serializable
import javax.persistence.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Immutable
@Table(name = "CAM_MMR")
data class ModuleRule(
  @EmbeddedId
  val key: ModuleRuleKey,

  @Column(name = "MMR_AYRC")
  @Type(type = "uk.ac.warwick.camcat.sits.types.AcademicYearType")
  val academicYear: AcademicYear?,

  @JoinColumn(name = "REX_CODE")
  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  val type: RuleType?,

  @JoinColumn(name = "RCL_CODE")
  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  val ruleClass: RuleClass?,

  @Column(name = "MMR_DESC")
  val description: String?,

  @OneToMany(fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  @JoinColumns(
    JoinColumn(name="MOD_CODE", referencedColumnName="MOD_CODE", insertable = false, updatable = false),
    JoinColumn(name="MMR_CODE", referencedColumnName="MMR_CODE", insertable = false, updatable = false)
  )
  val elements: Collection<ModuleRuleBody>
)


@Embeddable
data class ModuleRuleKey(
  @Column(name = "MOD_CODE")
  val moduleCode: String,

  @Column(name = "MMR_CODE")
  val moduleRuleCode: String
) : Serializable
