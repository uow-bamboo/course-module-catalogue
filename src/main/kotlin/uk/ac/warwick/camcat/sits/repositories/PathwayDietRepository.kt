package uk.ac.warwick.camcat.sits.repositories

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.ac.warwick.camcat.sits.entities.PathwayDiet
import uk.ac.warwick.camcat.sits.entities.Route
import uk.ac.warwick.util.termdates.AcademicYear

@Repository
interface PathwayDietRepository : CrudRepository<PathwayDiet, String> {
  @Query("from PathwayDiet where route = :route and academicYear = :academicYear")
  fun findPathwayDietsByRouteAndAcademicYear(route: Route, academicYear: AcademicYear): Collection<PathwayDiet>

}
