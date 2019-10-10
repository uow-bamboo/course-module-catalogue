package uk.ac.warwick.camcat.system.security

import org.springframework.security.web.authentication.WebAuthenticationDetails
import uk.ac.warwick.sso.client.SSOClientFilter.ACTUAL_USER_KEY
import uk.ac.warwick.sso.client.SSOClientFilter.USER_KEY
import javax.servlet.http.HttpServletRequest

class WarwickAuthenticationDetails(request: HttpServletRequest) : WebAuthenticationDetails(request) {
  val usercode: String = request.getAttribute("${USER_KEY}_usercode") as String
  val actualUsercode: String = request.getAttribute("${ACTUAL_USER_KEY}_usercode") as String
}

