package uk.ac.warwick.camcat.entities

import org.hibernate.annotations.AnyMetaDef
import java.time.OffsetDateTime
import java.util.*
import javax.persistence.*

@Entity
data class AuditEvent(
  @Id
  @GeneratedValue
  var id: UUID? = null,

  var operation: String,

  @Column(name = "event_date_utc")
  var date: OffsetDateTime,

  var usercode: String,

  var data: String?,

  @org.hibernate.annotations.Any(
    metaColumn = Column(name = "target_type")
  )
  @AnyMetaDef(
    metaType = "string",
    idType = "pg-uuid",
    metaValues = [
      // ...
    ]
  )
  @JoinColumn(name = "target_id")
  var target: AuditEventTarget?
)

interface AuditEventTarget {
  var id: UUID?
}
