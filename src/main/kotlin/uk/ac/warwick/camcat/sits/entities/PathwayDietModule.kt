package uk.ac.warwick.camcat.sits.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.*
import java.io.Serializable
import javax.persistence.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Immutable
@Table(name = "CAM_PDM")
data class PathwayDietModule(
  @EmbeddedId
  val key: PathwayDietModuleKey,

  @Column(name = "PDM_DESC")
  val description: String?,

  @Column(name = "PDM_IUSE")
  @Type(type = "yes_no")
  val inUse: Boolean?,

  @Convert(converter = ModuleSelectionConverter::class)
  @Column(name = "PDM_SESC")
  val selectionStatus: ModuleSelectionStatus?,

  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumnsOrFormulas(
    JoinColumnOrFormula(column = JoinColumn(name = "PDM_FMCC", referencedColumnName = "FMC_CODE")),
    JoinColumnOrFormula(formula = JoinFormula(value = "PDM_IUSE", referencedColumnName = "FMC_IUSE"))
  )
  @JsonIgnore
  val formedModuleCollection: FormedModuleCollection?
)

@Embeddable
data class PathwayDietModuleKey(
  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "PDM_PDTC", referencedColumnName = "PDT_CODE")
  @JsonIgnore
  val pathwayDiet: PathwayDiet,

  @Column(name = "PDM_SEQN")
  val sequence: String
) : Serializable {
  val pathwayDietCode: String
    get() = pathwayDiet.code
}
