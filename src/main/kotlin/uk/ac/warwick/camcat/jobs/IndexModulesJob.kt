package uk.ac.warwick.camcat.jobs

import org.quartz.DisallowConcurrentExecution
import org.quartz.Job
import org.quartz.JobExecutionContext
import uk.ac.warwick.camcat.search.services.ModuleSearchIndexingService

@DisallowConcurrentExecution
class IndexModulesJob(private val service: ModuleSearchIndexingService) : Job {
  override fun execute(context: JobExecutionContext?) {
    service.indexModules()
  }
}
