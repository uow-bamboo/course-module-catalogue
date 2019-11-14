package uk.ac.warwick.camcat.controllers

import org.quartz.Scheduler
import org.quartz.TriggerBuilder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import uk.ac.warwick.camcat.system.security.Role
import javax.annotation.security.RolesAllowed

@Controller
@RequestMapping("/sysadmin")
@RolesAllowed(Role.sysadmin)
class SysadminController(
  private val scheduler: Scheduler
) {
  @GetMapping
  fun home(): ModelAndView {
    scheduler.scheduleJob(
      TriggerBuilder.newTrigger()
        .forJob("IndexModulesJob", "Indexing")
        .startNow()
        .build()
    )
    return ModelAndView("sysadmin/home")
  }
}
