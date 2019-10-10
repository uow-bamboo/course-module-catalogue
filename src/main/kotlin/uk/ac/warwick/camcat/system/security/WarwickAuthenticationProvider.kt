package uk.ac.warwick.camcat.system.security

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class WarwickAuthenticationProvider(private val authenticationManager: WarwickAuthenticationManager) :
  AuthenticationProvider {
  override fun authenticate(authentication: Authentication?): Authentication? =
    authenticationManager.authenticate(authentication)

  override fun supports(authentication: Class<*>?): Boolean =
    authentication?.isAssignableFrom(WarwickAuthentication::class.java) ?: false
}
