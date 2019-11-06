package uk.ac.warwick.camcat.sits.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.hibernate.annotations.Immutable
import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction
import org.hibernate.annotations.Type
import uk.ac.warwick.camcat.system.serializers.AcademicYearSerializer
import uk.ac.warwick.util.termdates.AcademicYear
import java.io.Serializable
import javax.persistence.*

@Entity
@Immutable
@Table(schema = "INTUIT", name = "CAM_PDM")
data class PathwayDietModule(
  @EmbeddedId
  val key: PathwayDietModuleKey,

  @Column(name = "PDM_DESC")
  val description: String?,

  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "PDM_FMCC", referencedColumnName = "FMC_CODE")
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
