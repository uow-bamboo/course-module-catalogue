package uk.ac.warwick.camcat.system.context

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter
import uk.ac.warwick.sso.client.SSOClientFilter
import javax.servlet.http.HttpServletRequest

class WarwickPreAuthenticatedProcessingFilter : AbstractPreAuthenticatedProcessingFilter() {
  override fun getPreAuthenticatedPrincipal(request: HttpServletRequest?): Any? {
    val user = SSOClientFilter.getUserFromRequest(request)

    return if (user.isFoundUser) user.userId else null
  }

  override fun getPreAuthenticatedCredentials(request: HttpServletRequest?): Any {
    return "N/A"
  }
}
