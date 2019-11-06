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
@Table(schema = "INTUIT", name = "SRS_CRN")
data class CourseNotes(
  @EmbeddedId
  val key: CourseNotesKey,

  @Column(name = "CRN_AYRC")
  @Type(type = "uk.ac.warwick.camcat.sits.types.AcademicYearType")
  @JsonSerialize(using = AcademicYearSerializer::class)
  @JsonIgnore
  val academicYear: AcademicYear?,

  @Column(name = "CRN_DVNC")
  val descriptionVersion: String,

  @Column(name = "CRN_TITL")
  val title: String?,

  @Column(name = "CRN_DESC")
  val description: String?
) {
  val acadYear: String
    get() = academicYear.toString()
}

@Embeddable
data class CourseNotesKey(
  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "CRN_CRSC")
  val course: Course,

  @Column(name = "CRN_SEQN")
  val sequence: String
) : Serializable
