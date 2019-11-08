package uk.ac.warwick.camcat.sits.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.*
import uk.ac.warwick.util.termdates.AcademicYear
import javax.persistence.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Immutable
@Table(name = "CAM_PDT")
data class PathwayDiet(

  @Id
  @Column(name = "PDT_CODE")
  val code: String,

  @Column(name = "PDT_TYPE")
  val type: String,

  /**
   * Auto/Manual Diet Generation (1 character)
   * Determines whether the process is run automatically or manually.
   * If Automatic generation is selected, the  Prescribed Diet is automatically matched against students with
   * corresponding details within the GSD screen.  If Manual generation is selected
   * the Prescribed Diet will not be available to the student via the GSD screen
   * and must therefore be manually applied to the students via the Edit Module Diet (EMD)
   * screen. The option 'O' ('Online') gives rise to Automatic generation and is for Diets,
   * none of whose PDMs is compulsory; in other words, that Diet contains 'or' options
   * amongst its PDMs such that not one of the PDMs can be guaranteed to be part of the
   * eventual Diet; in this case every PDM will give rise to a SME and the student will
   * be forced to select every part of the Diet via e:Vision Module Registration.
   */
  @Column(name = "PDT_MATC")
  val generation: String,

  @Column(name = "PDT_AYRC")
  @Type(type = "uk.ac.warwick.camcat.sits.types.AcademicYearType")
  val academicYear: AcademicYear?,

  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "PDT_PRGC", referencedColumnName = "PRG_CODE")
  @JsonIgnore
  val programme: Programme?,

  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "PDT_PWYC", referencedColumnName = "PWY_CODE")
  @JsonIgnore
  val pathway: Pathway?,

  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "PDT_ROUC", referencedColumnName = "ROU_CODE")
  @JsonIgnore
  val route: Route?,

  @Column(name = "PDT_PRDC")
  val period: String?,

  @OneToMany(fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  @JoinColumn(name = "PDM_PDTC", referencedColumnName = "PDT_CODE")
  val pathwayDietModules: Collection<PathwayDietModule>?
) {
  val programmeCode: String?
    get() = programme?.code

  val pathwayCode: String?
    get() = pathway?.code

  val routeCode: String?
    get() = route?.code
}
