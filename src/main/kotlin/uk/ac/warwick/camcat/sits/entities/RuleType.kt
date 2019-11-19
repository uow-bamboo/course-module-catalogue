package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Immutable
@Table(name = "CAM_REX")
data class RuleType(
  @Id
  @Column(name = "REX_CODE")
  val code: String,

  @Column(name = "REX_NAME")
  val name: String?
) {
  companion object {
    const val PreRequisite = "PRE"
    const val PostRequisite = "POST"
    const val AntiRequisite = "ANTI"
  }
}
