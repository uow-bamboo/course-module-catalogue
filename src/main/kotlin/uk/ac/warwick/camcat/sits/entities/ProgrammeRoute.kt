package uk.ac.warwick.camcat.sits.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.Immutable
import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction
import org.hibernate.annotations.Type
import java.io.Serializable
import javax.persistence.*

@Entity
@Immutable
@Table(schema = "INTUIT", name = "INS_PRU")
data class ProgrammeRoute(
  @EmbeddedId
  val key: ProgrammeRouteKey,

  @Column(name = "PRU_IUSE")
  @Type(type = "yes_no")
  val inuse: Boolean?
)

@Embeddable
data class ProgrammeRouteKey(
  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "PRG_CODE", referencedColumnName = "PRG_CODE")
  val programme: Programme,

  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "ROU_CODE")
  @JsonIgnore
  val route: Route
) : Serializable {
  val routeCode: String
    get() = route.code
}
