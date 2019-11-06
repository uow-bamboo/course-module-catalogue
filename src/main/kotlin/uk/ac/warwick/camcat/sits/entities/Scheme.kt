package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.Immutable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Immutable
@Table(schema = "INTUIT", name = "INS_SCH")
data class Scheme(
  @Id
  @Column(name = "SCH_CODE")
  val code: String,

  @Column(name = "SCH_SNAM")
  val shortName: String?,

  @Column(name = "SCH_NAME")
  val name: String?,

  @Column(name = "SCH_CATS")
  val catsScheme: String?,

  @Column(name = "SCH_EREF")
  val externalReference: String?,

  @Column(name = "SCH_FTE")
  val fte: Int?,

  @Column(name = "SCH_ECCF")
  val externalCreditConversionFactor: Int?
)

