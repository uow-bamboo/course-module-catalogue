package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
import org.hibernate.annotations.Where
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Immutable
@Table(schema = "INTUIT", name = "SRS_CRS")
@Where(clause = "CRS_IUSE = 'Y'")
data class Course(
  @Id
  @Column(name = "CRS_CODE")
  val code: String,

  @Column(name = "CRS_TITL")
  val title: String?,

  @Column(name = "CRS_IUSE")
  val inUse: Boolean?
)