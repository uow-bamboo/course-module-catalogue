package uk.ac.warwick.camcat.sits.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.Immutable
import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction
import java.io.Serializable
import javax.persistence.*

@Entity
@Immutable
@Table(schema = "INTUIT", name = "SRS_CBK")
data class CourseBlock(
  @EmbeddedId
  val key: CourseBlockKey,

  @Column(name = "CBK_FESR")
  val reference: String?,

  @Column(name = "CBK_KSYR")
  val keyInformationSetCourseYear: Int?, // aka KIS Course Year

  @Column(name = "CBK_KUSE")
  val useForKeyInformationSet: Boolean?,

  @Column(name = "CBK_YEAR")
  val yearOfCourse: Int?
)

@Embeddable
data class CourseBlockKey(
  @Column(name = "CBK_BLOK")
  val block: String,

  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "CBK_CRSC", referencedColumnName = "CRS_CODE")
  @JsonIgnore
  val course: Course
) : Serializable {
  val courseCode: String
    get() = course.code
}
