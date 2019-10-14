package uk.ac.warwick.camcat.system.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

object Authority {
  val admin: GrantedAuthority = SimpleGrantedAuthority("ROLE_ADMIN")
  val sysadmin: GrantedAuthority = SimpleGrantedAuthority("ROLE_SYSADMIN")
  val masquerader: GrantedAuthority = SimpleGrantedAuthority("ROLE_MASQUERADER")

  val user: GrantedAuthority = SimpleGrantedAuthority("ROLE_USER")
  val applicant: GrantedAuthority = SimpleGrantedAuthority("ROLE_APPLICANT")
  val student: GrantedAuthority = SimpleGrantedAuthority("ROLE_STUDENT")
  val alumni: GrantedAuthority = SimpleGrantedAuthority("ROLE_ALUMNI")
  val staff: GrantedAuthority = SimpleGrantedAuthority("ROLE_STAFF")
}
