package uk.ac.warwick.camcat.sits.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.Immutable
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
  @JoinColumn(name = "MOD_CODE")
  @JsonIgnore
  val module: Module?,

  @ManyToOne
  @JoinColumn(name = "DPT_CODE")
  val teachingDepartment: Department?
)
