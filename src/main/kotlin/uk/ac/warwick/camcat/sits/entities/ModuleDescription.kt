package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
import java.io.Serializable
import javax.persistence.*

@Entity
@Immutable
@Table(schema = "INTUIT", name = "CAM_MDS")
data class ModuleDescription(
  @EmbeddedId
  val key: ModuleDescriptionKey,

  @Column(name = "MDS_AYRC")
  val academicYear: String?,

  @Column(name = "MDS_DVNC")
  val dvnc: String,

  @Column(name = "MDS_TITL")
  val title: String?,

  @Column(name = "MOD_DESC")
  val description: String?
)

@Embeddable
data class ModuleDescriptionKey(
  @Column(name = "MOD_CODE")
  val moduleCode: String,

  @Column(name = "MDS_SEQN")
  val sequence: String
) : Serializable
