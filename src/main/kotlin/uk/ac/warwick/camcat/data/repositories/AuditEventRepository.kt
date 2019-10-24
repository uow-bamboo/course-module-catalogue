package uk.ac.warwick.camcat.data.repositories

import org.springframework.data.repository.CrudRepository
import uk.ac.warwick.camcat.data.entities.AuditEvent
import java.util.*

interface AuditEventRepository : CrudRepository<AuditEvent, UUID>
