package uk.ac.warwick.camcat.system.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import uk.ac.warwick.userlookup.User

class WarwickUserDetails(val user: User, private val authorities: MutableCollection<out GrantedAuthority>) :
  UserDetails {
  override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authorities

  override fun isEnabled(): Boolean = !user.isLoginDisabled

  override fun getUsername(): String = user.userId

  override fun isCredentialsNonExpired(): Boolean = true

  override fun getPassword(): String? = null

  override fun isAccountNonExpired(): Boolean = true

  override fun isAccountNonLocked(): Boolean = !user.isLoginDisabled
}
