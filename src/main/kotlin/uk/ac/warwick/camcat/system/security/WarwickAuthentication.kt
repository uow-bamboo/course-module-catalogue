package uk.ac.warwick.camcat.system.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import uk.ac.warwick.userlookup.User

class WarwickAuthentication(
  userDetails: WarwickUserDetails,
  actualUserDetails: WarwickUserDetails
) : Authentication {
  val user: User = userDetails.user
  val actualUser: User = actualUserDetails.user

  private val grantedAuthorities: MutableCollection<GrantedAuthority> = mutableSetOf()

  init {
    grantedAuthorities.addAll(userDetails.authorities)
    grantedAuthorities.addAll(actualUserDetails.authorities)
  }

  override fun getAuthorities(): MutableCollection<out GrantedAuthority> = grantedAuthorities

  override fun setAuthenticated(isAuthenticated: Boolean) {
  }

  override fun getName(): String = user.fullName

  override fun getCredentials(): Any? = null

  override fun getPrincipal(): Any = user.userId

  override fun isAuthenticated(): Boolean = user.isFoundUser && !user.isLoginDisabled

  override fun getDetails(): Any = user
}
