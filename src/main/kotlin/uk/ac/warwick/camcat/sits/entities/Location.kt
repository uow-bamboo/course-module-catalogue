package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Immutable
@Table(name = "INS_LCA")
data class Location(
  @Id
  @Column(name = "LCA_CODE")
  val code: String,

  @Column(name = "LCA_NAME")
  val name: String?
)
