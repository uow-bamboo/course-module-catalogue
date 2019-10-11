package uk.ac.warwick.camcat.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.view.RedirectView
import uk.ac.warwick.camcat.system.security.Role
import javax.annotation.security.RolesAllowed
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/masquerade")
@RolesAllowed(Role.masquerader)
class MasqueradeController {
  @GetMapping
  fun form() = ModelAndView("masquerade/form")

  @PostMapping
  fun masquerade(@RequestParam username: String, response: HttpServletResponse): RedirectView {
    val cookie = Cookie("masqueradeAs", username)
    cookie.path = "/"
    cookie.secure = true
    cookie.isHttpOnly = true

    response.addCookie(cookie)

    return RedirectView("/masquerade")
  }
}
