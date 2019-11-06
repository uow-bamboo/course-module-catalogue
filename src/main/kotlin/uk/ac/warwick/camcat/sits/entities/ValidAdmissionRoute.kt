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
@Table(schema = "INTUIT", name = "SRS_VAR")
data class ValidAdmissionRoute(
  @EmbeddedId
  val key: ValidAdmissionRouteKey,

  @Column(name = "VAR_PRGC")
  val programmeOfStudyCode: String,

  @ManyToOne
  @JoinColumn(name = "VAR_ROUC")
  @NotFound(action = NotFoundAction.IGNORE)
  val route: Route,

  @Column(name = "VAR_IUSE")
  @Type(type = "yes_no")
  val inUse: Boolean?
)

@Embeddable
data class ValidAdmissionRouteKey(
  @ManyToOne
  @JoinColumn(name = "VAR_MCRC")
  @NotFound(action = NotFoundAction.IGNORE)
  @JsonIgnore
  val masCourse: MasCourse,

  @Column(name = "VAR_SEQN")
  val sequence: String
) : Serializable {
  val masCourseMcrCode: String
    get() = masCourse.mcrCode
}
