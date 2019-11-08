package uk.ac.warwick.camcat.sits.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.hibernate.annotations.Immutable
import org.hibernate.annotations.Type
import javax.persistence.*

@Entity
@Immutable
@Table(name = "INS_ROU")
data class Route(
  @Id
  @Column(name = "ROU_CODE")
  val code: String,

  @Column(name = "ROU_NAME")
  val name: String,

  @Column(name = "ROU_GSPY")
  @Type(type = "yes_no")
  val generatePathway: Boolean?,

  @Column(name = "ROU_UROE")
  @Type(type = "yes_no")
  val useRouteElement: Boolean?,

  @Column(name = "ROU_IUSE")
  @Type(type = "yes_no")
  val inUse: Boolean?,

  @OneToMany(fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  @JoinColumn(referencedColumnName = "ROU_CODE", name = "VCO_ROUC")
  val validCourseOptions: Collection<ValidCourseOption>?,

  @OneToMany(fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  @JoinColumn(referencedColumnName = "ROU_CODE", name = "ROE_ROUC")
  val routeElements: Collection<RouteElement>?,

  @OneToMany(fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  @JoinColumn(referencedColumnName = "ROU_CODE", name = "ROU_CODE")
  val programmeRoutes: Collection<ProgrammeRoute>?,

  @OneToMany(fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  @JoinColumn(name = "PDT_ROUC", referencedColumnName = "ROU_CODE")
  @JsonIgnore
  val pathwayDiets: Collection<PathwayDiet>?
)
