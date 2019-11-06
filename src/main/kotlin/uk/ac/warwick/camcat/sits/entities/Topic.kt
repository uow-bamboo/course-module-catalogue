package uk.ac.warwick.camcat.sits.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.Immutable
import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction
import javax.persistence.*

@Entity
@Immutable
@Table(schema = "INTUIT", name = "CAM_TOP")
data class Topic(
  @Id
  @Column(name = "TOP_CODE")
  val code: String,

  @Column(name = "TOP_PERC")
  val percentage: Int?,

  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "MOD_CODE")
  @JsonIgnore
  val module: Module?,

  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "DPT_CODE")
  val teachingDepartment: Department?
) {
  val moduleCode: String?
    get() = module?.code
}
