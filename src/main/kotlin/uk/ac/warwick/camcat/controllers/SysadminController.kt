package uk.ac.warwick.camcat.controllers

import org.quartz.JobKey.jobKey
import org.quartz.Scheduler
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.View
import org.springframework.web.servlet.view.RedirectView
import uk.ac.warwick.camcat.system.security.Role
import javax.annotation.security.RolesAllowed

@Controller
@RequestMapping("/sysadmin")
@RolesAllowed(Role.sysadmin)
class SysadminController {
  @GetMapping
  fun home() = ModelAndView("sysadmin/home")
}

@Controller
@RequestMapping("/sysadmin/index/modules")
@RolesAllowed(Role.sysadmin)
class TriggerIndexModulesJobController(
  private val scheduler: Scheduler
) {
  @PostMapping
  fun trigger(): View {
    scheduler.triggerJob(jobKey("IndexModulesJob", "Indexing"))

    return RedirectView("/sysadmin/index/modules")
  }

  @GetMapping
  fun ok() = ModelAndView(
    "alert", mapOf(
      "class" to "success",
      "title" to "Job triggered",
      "icon" to "check-circle",
      "message" to "Triggered the module indexing job",
      "continue" to "/sysadmin"
    )
  )
}
