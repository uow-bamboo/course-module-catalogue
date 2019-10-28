package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Immutable
@Table(schema = "INTUIT", name = "CAM_LEV")
data class Level(
  @Id
  @Column(name = "LEV_CODE")
  val code: String,

  @Column(name = "LEV_NAME")
  val name: String?
)
