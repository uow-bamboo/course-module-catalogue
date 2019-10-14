package uk.ac.warwick.camcat.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import uk.ac.warwick.camcat.system.security.Role
import javax.annotation.security.RolesAllowed

@Controller
@RequestMapping("/sysadmin")
@RolesAllowed(Role.sysadmin)
class SysadminController {
  @GetMapping
  fun home() = ModelAndView("sysadmin/home")
}
