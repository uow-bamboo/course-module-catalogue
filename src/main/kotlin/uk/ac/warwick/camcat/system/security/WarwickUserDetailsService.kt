package uk.ac.warwick.camcat.system.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import uk.ac.warwick.camcat.system.config.RoleConfiguration
import uk.ac.warwick.userlookup.GroupService
import uk.ac.warwick.userlookup.User
import uk.ac.warwick.userlookup.UserLookup
import uk.ac.warwick.userlookup.UserLookupInterface

@Service
class WarwickUserDetailsService(
  private val userLookup: UserLookupInterface,
  private val groupService: GroupService,
  private val roleConfig: RoleConfiguration
) : UserDetailsService {
  override fun loadUserByUsername(username: String?): UserDetails {
    val user = userLookup.getUserByUserId(username)

    return WarwickUserDetails(user, authorities = getAuthorities(user))
  }

  private fun getAuthorities(user: User): MutableList<GrantedAuthority> {
    val authorities = mutableListOf<GrantedAuthority>()

    if (user.isFoundUser)
      authorities += Authority.user

    if (user.isStaff)
      authorities += Authority.staff

    if (user.isStudent)
      authorities += Authority.student

    if (user.isAlumni)
      authorities += Authority.alumni

    if (user.isApplicant)
      authorities += Authority.applicant

    if (groupService.isUserInGroup(user.userId, roleConfig.masqueradersWebgroup))
      authorities += Authority.masquerader

    if (groupService.isUserInGroup(user.userId, roleConfig.sysadminWebgroup))
      authorities += Authority.sysadmin

    if (groupService.isUserInGroup(user.userId, roleConfig.adminWebgroup))
      authorities += Authority.admin

    return authorities
  }
}
