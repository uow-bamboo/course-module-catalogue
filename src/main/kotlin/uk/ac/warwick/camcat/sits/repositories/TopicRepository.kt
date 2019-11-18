package uk.ac.warwick.camcat.sits.repositories

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import uk.ac.warwick.camcat.sits.entities.Topic
import uk.ac.warwick.util.termdates.AcademicYear

interface TopicRepository : CrudRepository<Topic, String> {
  @Query("from Topic where module.code = :moduleCode and academicYear = :academicYear")
  fun findByModuleCodeAndAcademicYear(moduleCode: String, academicYear: AcademicYear?): Collection<Topic>

  @Query("from Topic where module.code = :moduleCode and inUse = true and academicYear is null")
  fun findInUseByModuleCodeWhereAcademicYearIsNull(moduleCode: String): Collection<Topic>
}
