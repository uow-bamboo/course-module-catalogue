package uk.ac.warwick.camcat.sits.repositories

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import uk.ac.warwick.camcat.sits.entities.Department

interface DepartmentRepository : CrudRepository<Department, String> {
  @Query("from Department where inUse = true and type in ('SCH', 'DEP') order by name")
  fun findAllAcademicDepartments(): Iterable<Department>
}
