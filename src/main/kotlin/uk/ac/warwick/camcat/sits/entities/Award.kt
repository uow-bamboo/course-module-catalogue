package uk.ac.warwick.camcat.sits.entities

import org.hibernate.annotations.*
import javax.persistence.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Immutable
@Table(name = "INS_AWD")
@Where(clause = "AWD_IUSE = 'Y'")
data class Award(
  @Id
  @Column(name = "AWD_CODE")
  val code: String,

  @Column(name = "AWD_NAME")
  val name: String?,

  @Column(name = "AWD_IUSE")
  @Type(type = "yes_no")
  val inUse: Boolean?,

  @Column(name = "AWD_DESC")
  val description: String?,

  @Column(name = "AWD_UDFJ")
  val hearLevel: String?, // HEAR 3.1 qualification level

  @Column(name = "AWD_EQAC")
  val externalQualificationAimCode: String?,

  @Column(name = "VAL_CODE")
  val validatingBodyCode: String?,

  @ManyToMany(
    mappedBy = "awards"
  )
  @NotFound(action = NotFoundAction.IGNORE)
  @Fetch(FetchMode.SELECT)
  val programmes: Collection<Programme>
)
