package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.*
import uk.ac.warwick.util.termdates.AcademicYear
import java.io.Serializable
import javax.persistence.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Immutable
@Table(name = "CAM_PDT")
@Where(clause = "PDT_CODE like '%-%-%' and length(PDT_CODE) = 9")
data class PathwayDiet(
  @Id
  @Column(name = "PDT_CODE")
  val code: String,

  @Column(name = "PDT_IUSE")
  @Type(type = "yes_no")
  val inUse: Boolean?,

  @Column(name = "PDT_TYPE")
  val type: String,

//  this is sits's recommended way to get PDT's acadYear, but it only has value up to 08/09
//  @Column(name = "PDT_AYRC")
//  @Type(type = "uk.ac.warwick.camcat.sits.types.AcademicYearType")
//  val academicYear: AcademicYear?,

  // parse a acad year from PDT code, not that we wanted to do it this way
  @Formula("substr(PDT_CODE, 8, 2)")
  @Type(type = "uk.ac.warwick.camcat.sits.types.TwoDigitAcademicYearType")
  val academicYear: AcademicYear?,

  @Column(name = "PDT_BLOK")
  val block: String?,

  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "PDT_ROUC", referencedColumnName = "ROU_CODE")
  val route: Route?,

  @Column(name = "PDT_PRDC")
  val period: String?,

  @OneToMany
  @Fetch(FetchMode.SELECT)
  @JoinColumn(name = "PDM_PDTC", referencedColumnName = "PDT_CODE")
  val pathwayDietModules: Collection<PathwayDietModule>
) : Serializable
