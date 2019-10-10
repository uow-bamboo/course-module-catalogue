package uk.ac.warwick.camcat.system.security

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class WarwickAuthenticationManager(private val userDetailsService: WarwickUserDetailsService) :
  AuthenticationManager {
  override fun authenticate(auth: Authentication?): Authentication? {
    if (auth != null && auth !is WarwickAuthentication) {
      val details = auth.details as WarwickAuthenticationDetails
      val user = userDetailsService.loadUserByUsername(details.usercode) as WarwickUserDetails
      val actualUser = userDetailsService.loadUserByUsername(details.actualUsercode) as WarwickUserDetails

      return WarwickAuthentication(user, actualUser)
    }

    return null
  }
}
