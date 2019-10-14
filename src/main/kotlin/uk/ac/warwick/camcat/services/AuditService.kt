package uk.ac.warwick.camcat.services

import com.fasterxml.jackson.databind.json.JsonMapper
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import uk.ac.warwick.camcat.entities.AuditEvent
import uk.ac.warwick.camcat.entities.AuditEventTarget
import uk.ac.warwick.camcat.repositories.AuditEventRepository
import uk.ac.warwick.util.logging.AuditLogger
import java.time.OffsetDateTime

interface AuditService {
  fun <A> audit(operation: String, target: AuditEventTarget, data: Map<String, Any>?, op: () -> A): A
}

@Service
class AuditServiceImpl(
  private val repository: AuditEventRepository,
  private val auditLogger: AuditLogger
) : AuditService {
  override fun <A> audit(operation: String, target: AuditEventTarget, data: Map<String, Any>?, op: () -> A): A {
    val principal = SecurityContextHolder.getContext().authentication.principal as? String
      ?: throw IllegalStateException("Unknown principal for audit event")

    val json = data?.let { JsonMapper().writeValueAsString(it) }

    try {
      val result = op()

      auditLogger.log(
        AuditLogger.RequestInformation.forEventType(operation).withUsername(principal),
        data?.mapKeys { AuditLogger.Field(it.key) }
      )

      repository.save(
        AuditEvent(
          usercode = principal,
          operation = operation,
          target = target,
          date = OffsetDateTime.now(),
          data = json
        )
      )

      return result
    } catch (e: Exception) {
      throw e
    }
  }
}
