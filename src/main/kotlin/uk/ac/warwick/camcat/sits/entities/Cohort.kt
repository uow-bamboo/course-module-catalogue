package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Immutable
@Table(name = "SRS_COH")
data class Cohort(
  @Id
  @Column(name = "COH_CODE")
  val code: String,

  @Column(name = "COH_SNAM")
  val shortName: String?,

  @Column(name = "COH_NAME")
  val name: String?
)
