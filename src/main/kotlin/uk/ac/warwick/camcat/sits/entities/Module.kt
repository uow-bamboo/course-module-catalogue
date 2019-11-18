package uk.ac.warwick.camcat.sits.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.*
import java.math.BigDecimal
import javax.persistence.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Immutable
@Table(name = "INS_MOD")
@Where(clause = "MOD_CODE LIKE '%-%'")
data class Module(
  @Id
  @Column(name = "MOD_CODE")
  val code: String,

  @Column(name = "MOD_NAME")
  val title: String?,

  @JoinColumn(name = "DPT_CODE")
  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  val department: Department?,

  @JoinColumn(name = "MAP_CODE")
  @ManyToOne
  @NotFound(action = NotFoundAction.IGNORE)
  val assessmentPattern: AssessmentPattern?,

  @Column(name = "MOD_CRDT")
  val creditValue: BigDecimal?,

  @OneToMany
  @Fetch(FetchMode.SELECT)
  @NotFound(action = NotFoundAction.IGNORE)
  @JoinColumn(name = "FME_MODP", referencedColumnName = "MOD_CODE")
  @JsonIgnore
  val formedModuleCollectionElements: Collection<FormedModuleCollectionElement>
)
