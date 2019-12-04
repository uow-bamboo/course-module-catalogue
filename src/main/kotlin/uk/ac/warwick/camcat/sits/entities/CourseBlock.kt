package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction
import org.hibernate.annotations.Type
import java.io.Serializable
import javax.persistence.*

@Entity
@Immutable
@Table(name = "SRS_CBK")
data class CourseBlock(
  @EmbeddedId
  val key: CourseBlockKey,

  @Column(name = "CBK_FESR")
  val reference: String?,

  @Column(name = "CBK_KSYR")
  val keyInformationSetCourseYear: Int?, // aka KIS Course Year

  @Column(name = "CBK_KUSE")
  @Type(type = "yes_no")
  val useForKeyInformationSet: Boolean?,

  @Column(name = "CBK_YEAR")
  val yearOfCourse: Int?,

  @OneToMany
  @JoinColumns(
    JoinColumn(name = "CBO_CRSC", referencedColumnName = "CBK_CRSC", insertable = false, updatable = false),
    JoinColumn(name = "CBO_BLOK", referencedColumnName = "CBK_BLOK", insertable = false, updatable = false)
  )
  val occurrences: Collection<CourseBlockOccurrence>
)

@Embeddable
data class CourseBlockKey(
  @Column(name = "CBK_BLOK")
  val block: String,

  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "CBK_CRSC", referencedColumnName = "CRS_CODE")
  val course: Course
) : Serializable
