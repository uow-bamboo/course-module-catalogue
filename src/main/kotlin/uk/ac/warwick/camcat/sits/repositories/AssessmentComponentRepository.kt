package uk.ac.warwick.camcat.sits.repositories

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import uk.ac.warwick.camcat.sits.entities.AssessmentComponent
import uk.ac.warwick.camcat.sits.entities.AssessmentComponentKey
import uk.ac.warwick.util.termdates.AcademicYear

interface AssessmentComponentRepository : CrudRepository<AssessmentComponent, AssessmentComponentKey> {
  @Query(
    """
    select c from AssessmentComponent c
    left join fetch c.type
    left join fetch c.description
    left join fetch c.paper
    left join fetch c.paperDivision
    where c.key.assessmentPatternCode = :assessmentPatternCode
    and c.reassessment = false
    and c.introducedAcademicYear = (select max(introducedAcademicYear) from AssessmentComponent where key.assessmentPatternCode = :assessmentPatternCode and introducedAcademicYear <= :academicYear)
    order by c.key.sequence
  """
  )
  fun findAllByAssessmentPatternCodeAndAcademicYear(assessmentPatternCode: String, academicYear: AcademicYear): List<AssessmentComponent>
}
