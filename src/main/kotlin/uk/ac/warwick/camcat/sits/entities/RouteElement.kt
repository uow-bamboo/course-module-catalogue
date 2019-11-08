package uk.ac.warwick.camcat.sits.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.Immutable
import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction
import java.io.Serializable
import javax.persistence.*

@Entity
@Immutable
@Table(name = "INS_ROE")
data class RouteElement(
  @EmbeddedId
  val key: RouteElementKey,

  @ManyToOne
  @JoinColumn(name = "ROE_PWYC")
  @NotFound(action = NotFoundAction.IGNORE)
  val pathway: Pathway?
)

@Embeddable
data class RouteElementKey(
  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "ROE_ROUC")
  @JsonIgnore
  val route: Route,

  @Column(name = "ROE_SEQ3")
  val sequence: String
) : Serializable {
  val routeCode: String
    get() = route.code
}
