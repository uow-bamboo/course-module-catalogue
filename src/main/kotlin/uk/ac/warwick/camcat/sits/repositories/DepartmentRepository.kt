package uk.ac.warwick.camcat.sits.repositories

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import uk.ac.warwick.camcat.sits.entities.Department

interface DepartmentRepository : CrudRepository<Department, String> {
  @Query("""
    select d from Department d
    join fetch d.faculty
    where d.type in ('DEP', 'INS', 'SCH')
    and (d.modules is not empty or d.courses is not empty)
    order by d.name
  """)
  fun findAllAcademicDepartments(): Iterable<Department>
}
