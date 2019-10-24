package uk.ac.warwick.camcat.sits.repositories

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import uk.ac.warwick.camcat.sits.entities.Module

@Repository
interface ModuleRepository : CrudRepository<Module, String>
