package uk.ac.warwick.camcat.sits.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.*
import javax.persistence.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Immutable
// known and referred to as SRS_PWY, but actual tabla is INS_PWY
@Table(schema = "INTUIT", name = "INS_PWY")
data class Pathway(

  @Id
  @Column(name = "PWY_CODE")
  val code: String,

  @Column(name = "PWY_SNAM")
  val shortName: String?,

  @Column(name = "PWY_NAME")
  val name: String?,

  @Column(name = "PWY_AWDN")
  val awardPrintName: String?,

  @Column(name = "PWY_PWGC")
  val pathwayGroupCode: String?,

  @ManyToOne
  @JoinColumn(name = "PWY_ESBC")
  @NotFound(action = NotFoundAction.IGNORE)
  @JsonIgnore
  val externalSubject1: ExternalSubject?,

  @ManyToOne
  @JoinColumn(name = "PWY_ESB2")
  @JsonIgnore
  @NotFound(action = NotFoundAction.IGNORE)
  val externalSubject2: ExternalSubject?,

  @ManyToOne
  @JoinColumn(name = "PWY_ESB3")
  @JsonIgnore
  @NotFound(action = NotFoundAction.IGNORE)
  val externalSubject3: ExternalSubject?,

  @Column(name = "PWY_EBAL")
  val externalSubjectBalance: String?,

  @OneToMany(fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  @JoinColumn(name = "PDT_PWYC", referencedColumnName = "PWY_CODE")
  val pathwayDiets: Collection<PathwayDiet>?
) {
  val externalSubjects: List<ExternalSubject>
    get() = listOfNotNull(externalSubject1, externalSubject2, externalSubject3)
}
