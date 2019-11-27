package uk.ac.warwick.camcat.sits.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.*
import javax.persistence.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Immutable
@Table(name = "INS_ROU")
data class Route(
  @Id
  @Column(name = "ROU_CODE")
  val code: String,

  @Column(name = "ROU_NAME")
  val name: String?,

  @Column(name = "ROU_IUSE")
  @Type(type = "yes_no")
  val inUse: Boolean?,

  @ManyToMany
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

  @ManyToMany(mappedBy = "routes")
  @NotFound(action = NotFoundAction.IGNORE)
  @Fetch(FetchMode.SELECT)
  @LazyCollection(LazyCollectionOption.FALSE)
  @JsonIgnore
  val courses: Collection<Course>,

  @OneToMany
  @Fetch(FetchMode.SELECT)
  @JoinColumn(name = "PDT_ROUC", referencedColumnName = "ROU_CODE")
  @JsonIgnore
  val pathwayDiets: Collection<PathwayDiet>
)
