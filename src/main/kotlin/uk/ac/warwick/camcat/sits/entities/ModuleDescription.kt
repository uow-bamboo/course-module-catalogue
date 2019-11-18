package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
import org.hibernate.annotations.Type
import uk.ac.warwick.util.termdates.AcademicYear
import java.io.Serializable
import javax.persistence.*

@Entity
@Immutable
@Table(name = "CAM_MDS")
data class ModuleDescription(
  @EmbeddedId
  val key: ModuleDescriptionKey,

  @Column(name = "MDS_AYRC")
  @Type(type = "uk.ac.warwick.camcat.sits.types.AcademicYearType")
  val academicYear: AcademicYear?,

  @Column(name = "MDS_DVNC")
  val code: String,

  @Column(name = "MDS_TITL")
  val title: String?,

  @Column(name = "MOD_DESC")
  val description: String?,

  @Column(name = "MDS_UDF1")
  val udf1: String?,

  @Column(name = "MDS_UDF2")
  val udf2: String?,

  @Column(name = "MDS_UDF3")
  val udf3: String?,

  @Column(name = "MDS_UDF4")
  val udf4: String?,

  @Column(name = "MDS_UDF5")
  @Type(type = "yes_no")
  val udf5: Boolean?
)

@Embeddable
data class ModuleDescriptionKey(
  @Column(name = "MOD_CODE")
  val moduleCode: String,

  @Column(name = "MDS_SEQN")
  val sequence: String
) : Serializable
