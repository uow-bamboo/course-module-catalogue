package uk.ac.warwick.camcat.services

import com.fasterxml.jackson.databind.json.JsonMapper
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import uk.ac.warwick.camcat.entities.AuditEvent
import uk.ac.warwick.camcat.entities.AuditEventTarget
import uk.ac.warwick.camcat.repositories.AuditEventRepository
import java.time.OffsetDateTime

@Service
class AuditService(private val repository: AuditEventRepository) {
  fun <A> audit(operation: String, target: AuditEventTarget, data: Any?, op: () -> A): A {
    val principal = SecurityContextHolder.getContext().authentication.principal as? String
      ?: throw IllegalStateException("Unknown principal for audit event")

    val json = data?.let { JsonMapper().writeValueAsString(it) }

    try {
      val result = op()

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
