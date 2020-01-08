package uk.ac.warwick.camcat.system.context

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter
import uk.ac.warwick.sso.client.SSOClientFilter
import uk.ac.warwick.userlookup.User
import javax.servlet.http.HttpServletRequest

class WarwickPreAuthenticatedProcessingFilter : AbstractPreAuthenticatedProcessingFilter() {
  override fun getPreAuthenticatedPrincipal(request: HttpServletRequest?): Any? {
    val user = request?.getAttribute(SSOClientFilter.USER_KEY) as User?

    return user?.takeIf { it.isFoundUser }?.userId
  }

  override fun getPreAuthenticatedCredentials(request: HttpServletRequest?): Any {
    return "N/A"
  }
}
