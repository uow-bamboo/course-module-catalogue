package uk.ac.warwick.camcat.repositories

import org.springframework.data.repository.CrudRepository
import uk.ac.warwick.camcat.entities.AuditEvent
import java.util.*

interface AuditEventRepository : CrudRepository<AuditEvent, UUID>
