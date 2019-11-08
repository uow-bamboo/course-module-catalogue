package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction
import java.io.Serializable
import javax.persistence.*

@Entity
@Immutable
@Table(name = "SRS_CSD")
data class CourseSubjectDepartment(
  @EmbeddedId
  val key: CourseSubjectDepartmentKey,

  @Column(name = "CSD_PERC")
  val percentage: Int?
)

@Embeddable
data class CourseSubjectDepartmentKey(
  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "CSD_CRSC")
  val course: Course,

  @Column(name = "CSD_BLOK")
  val block: String,

  @Column(name = "CSD_SUBC")
  val subjectCode: String,

  @ManyToOne
  @JoinColumn(name = "CSD_DPTC")
  @NotFound(action = NotFoundAction.IGNORE)
  val department: Department
) : Serializable
