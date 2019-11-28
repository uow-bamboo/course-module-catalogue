package uk.ac.warwick.camcat.sits.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.*
import java.io.Serializable
import java.math.BigDecimal
import javax.persistence.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Immutable
@Table(name = "INS_MOD")
@Where(clause = "MOD_UDF5 = 'Y'")
data class Module(
  @Id
  @Column(name = "MOD_CODE")
  val code: String,

  @Column(name = "MOD_NAME")
  val title: String?,

  @Column(name = "MOD_IUSE")
  @Type(type = "yes_no")
  val inUse: Boolean?,

  @JoinColumn(name = "DPT_CODE")
  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  val department: Department?,

  @JoinColumn(name = "MOD_CODE")
  @OneToMany
  @NotFound(action = NotFoundAction.IGNORE)
  @Fetch(FetchMode.SELECT)
  @JsonIgnore
  val topics: Collection<Topic>,

  @JoinColumn(name = "MAP_CODE")
  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  @JsonIgnore
  val assessmentPattern: AssessmentPattern?,

  @Column(name = "MOD_CRDT")
  val creditValue: BigDecimal?,

  @OneToMany
  @Fetch(FetchMode.SELECT)
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "FME_MODP", referencedColumnName = "MOD_CODE")
  @JsonIgnore
  val formedModuleCollectionElements: Collection<FormedModuleCollectionElement>,

  @OneToMany
  @Fetch(FetchMode.SELECT)
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "MOD_CODE")
  val occurrences: Collection<ModuleOccurrence>,

  @OneToMany
  @JoinColumn(name = "MOD_CODE")
  val descriptions: Collection<ModuleDescription>,

  @Column(name = "MOD_UDF5")
  @Type(type = "yes_no")
  val approved: Boolean?
) : Serializable {
  val assessmentPatternCode: String?
    get() = assessmentPattern?.code

  val topicCodes: Collection<String>
    get() = topics.map { it.code }
}
