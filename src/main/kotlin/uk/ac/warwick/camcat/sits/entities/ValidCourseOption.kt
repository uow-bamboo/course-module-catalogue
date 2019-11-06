package uk.ac.warwick.camcat.sits.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.Immutable
import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction
import java.io.Serializable
import javax.persistence.*

@Entity
@Immutable
@Table(schema = "INTUIT", name = "SRS_VCO")
data class ValidCourseOption(
  @EmbeddedId
  val key: ValidCourseOptionKey,

  @Column(name = "VCO_IUSE")
  val inUse: Boolean?,

  @Column(name = "VCO_PRGC")
  val programmeOfStudyCode: String,

  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "VCO_ROUC")
  @JsonIgnore
  val route: Route,

  @ManyToOne
  @JoinColumn(name = "VCO_SCHC")
  @NotFound(action = NotFoundAction.IGNORE)
  val scheme: Scheme?,

  @Column(name = "VCO_AWDC")
  val awardCode: String?,

  @Column(name = "VCO_NAME")
  val name: String?
) {
  val routeCode: String
    get() = route.code
}

@Embeddable
data class ValidCourseOptionKey(
  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "VCO_CRSC")
  @JsonIgnore
  val course: Course,

  @Column(name = "VCO_SEQ6")
  val sequence: String
) : Serializable {
  val courseCode: String
    get() = course.code
}
