package uk.ac.warwick.camcat.sits.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.*
import javax.persistence.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Immutable
@Table(name = "INS_ROU")
@Where(clause = "ROU_IUSE = 'Y'")
data class Route(
  @Id
  @Column(name = "ROU_CODE")
  val code: String,

  @Column(name = "ROU_NAME")
  val name: String,

//  @Column(name = "ROU_GSPY")
//  @Type(type = "yes_no")
//  val generatePathway: Boolean?,
//
//  @Column(name = "ROU_UROE")
//  @Type(type = "yes_no")
//  val useRouteElement: Boolean?,

  @Column(name = "ROU_IUSE")
  @Type(type = "yes_no")
  val inUse: Boolean?,
//
//  @OneToMany(fetch = FetchType.EAGER)
//  @Fetch(FetchMode.SELECT)
//  @JoinColumn(referencedColumnName = "ROU_CODE", name = "VCO_ROUC")
//  @JsonIgnore
//  val validCourseOptions: Collection<ValidCourseOption>?,

//  @OneToMany(fetch = FetchType.EAGER)
//  @Fetch(FetchMode.SELECT)
//  @JoinColumn(referencedColumnName = "ROU_CODE", name = "ROE_ROUC")
//  val routeElements: Collection<RouteElement>?,

//  @OneToMany(fetch = FetchType.EAGER)
//  @Fetch(FetchMode.SELECT)
//  @JoinColumn(referencedColumnName = "ROU_CODE", name = "ROU_CODE")
//  val programmeRoutes: Collection<ProgrammeRoute>?,

  @ManyToMany(fetch = FetchType.LAZY)
  @NotFound(action = NotFoundAction.IGNORE)
  @Fetch(FetchMode.SELECT)
  @JoinTable(
    name = "INS_PRU",
    joinColumns = [
      JoinColumn(name = "ROU_CODE")
    ],
    inverseJoinColumns = [
      JoinColumn(name = "PRG_CODE")
    ]
  )
  @JsonIgnore
  val programmes: Collection<Programme>,

  @ManyToMany(
    fetch = FetchType.EAGER,
    mappedBy = "routes"
  )
  @NotFound(action = NotFoundAction.IGNORE)
  @Fetch(FetchMode.SELECT)
  @LazyCollection(LazyCollectionOption.FALSE)
  @JsonIgnore
  val courses: Collection<Course>,

  @OneToMany(fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  @JoinColumn(name = "PDT_ROUC", referencedColumnName = "ROU_CODE")
  @JsonIgnore
  val pathwayDiets: Collection<PathwayDiet>
)
