package uk.ac.warwick.camcat.sits.repositories

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.ac.warwick.camcat.sits.entities.ModuleRule
import uk.ac.warwick.camcat.sits.entities.ModuleRuleKey

@Repository
interface ModuleRuleRepository : CrudRepository<ModuleRule, ModuleRuleKey>
