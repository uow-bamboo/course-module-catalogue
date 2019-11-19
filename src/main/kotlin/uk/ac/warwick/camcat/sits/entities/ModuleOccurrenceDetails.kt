package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
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
  val creditValue: Int?
)

