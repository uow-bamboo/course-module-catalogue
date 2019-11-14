package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Immutable
@Table(name = "CAM_MAVT")
data class ModuleOccurrenceDetails(
  @EmbeddedId
  val key: ModuleOccurrenceKey,

  @Column(name = "MAV_CRED")
  val creditValue: Int?,

  @Column(name = "MAV_SIZR")
  val maximumStudentNumber: Int?,

  @Column(name = "MAV_SSTH")
  val scheduledStudyHours: Int?,

  @Column(name = "MAV_PLAH")
  val placementStudyHours: Int?,

  @Column(name = "MAV_INDH")
  val independentStudyHours: Int?,

  @Column(name = "MAV_UDFK")
  val assessmentStudyHours: Int?
) : Serializable

