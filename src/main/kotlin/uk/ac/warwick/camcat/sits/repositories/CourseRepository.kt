package uk.ac.warwick.camcat.sits.repositories

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.ac.warwick.camcat.sits.entities.Course
import uk.ac.warwick.camcat.sits.entities.Module
import uk.ac.warwick.util.termdates.AcademicYear

@Repository
interface CourseRepository : CrudRepository<Course, String> {
  fun findAll(pageable: Pageable): Page<Course>

  fun findByCode(code: String): Course?

  @Query("""
    select courses from Module module
    join module.formedModuleCollectionElements formedModuleCollectionElements
    join formedModuleCollectionElements.key.formedModuleCollection formedModuleCollection
    join formedModuleCollection.pathwayDietModules pathwayDietModules
    join pathwayDietModules.key.pathwayDiet pathwayDiet
    join pathwayDiet.route route
    join route.courses courses
    join module.moduleOccurrences moduleOccurrences
    where module.code = :moduleCode
    and pathwayDiet.code like '%-%-%'
    and length(pathwayDiet.code) = 9
    and pathwayDiet.inUse = true 
    and moduleOccurrences.key.academicYear = :academicYear
    and pathwayDiet.academicYear = SUBSTRING(:academicYear, 1, 2)"""
    //              ^^ is a 2 digit starting year (e.g. 10), which is different to acadYear toString (e.g. 10/11)
  )
  fun findAllByModuleAndAcademicYear(moduleCode: String, academicYear: AcademicYear): Collection<Course>
}
