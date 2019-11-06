package uk.ac.warwick.camcat.system.context

import org.quartz.JobBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.quartz.CronTriggerFactoryBean
import uk.ac.warwick.camcat.jobs.IndexModulesJob

@Configuration
class SchedulingContext {
  @Bean
  fun indexModulesTrigger(): CronTriggerFactoryBean {
    val bean = CronTriggerFactoryBean()
    bean.setCronExpression("0 0 */2 * * ?")
    bean.setJobDetail(
      JobBuilder.newJob(IndexModulesJob::class.java)
        .withIdentity("IndexModulesJob", "Indexing")
        .storeDurably()
        .build()
    )
    return bean
  }
}
