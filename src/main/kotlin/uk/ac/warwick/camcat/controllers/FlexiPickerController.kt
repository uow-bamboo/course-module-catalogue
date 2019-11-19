package uk.ac.warwick.camcat.controllers

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.ac.warwick.camcat.services.GroupService
import uk.ac.warwick.camcat.system.security.Role
import uk.ac.warwick.userlookup.Group
import uk.ac.warwick.userlookup.User
import uk.ac.warwick.userlookup.UserLookup
import uk.ac.warwick.userlookup.UserLookupInterface
import javax.annotation.security.RolesAllowed

@RestController
@RequestMapping("/flexipicker")
@RolesAllowed(Role.user)
class FlexiPickerController(
  private val userLookup: UserLookupInterface,
  private val groupService: GroupService
) {
  @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
  fun query(@RequestBody q: FlexiPickerQuery): Map<*, *> {
    val queryString = q.query.trim()

    val results: MutableList<FlexiPickerResult> = mutableListOf()

    if (q.includeGroups) {
      groupService.getGroupByName(queryString)?.let {
        results += GroupFlexiPickerResult(it)
      }
    }

    if (q.includeUsers) {
      userLookup.getUserByUserId(queryString).takeIf { it.isFoundUser }?.let {
        results += UserFlexiPickerResult(it)
      }
    }

    if (q.includeUsers && queryString.matches(Regex("^[0-9]{7,}$"))) {
      userLookup.getUserByWarwickUniId(queryString, true).takeIf { it.isFoundUser }?.let {
        results += UserFlexiPickerResult(it)
      }
    }

    if (!q.exact && q.includeGroups) {
      groupService.getGroupsForQuery(queryString).forEach {
        results += GroupFlexiPickerResult(it)
      }
    }

    if (!q.exact && q.includeUsers) {
      queryUsers(q).forEach {
        results += UserFlexiPickerResult(it)
      }
    }

    return mapOf("data" to mapOf("results" to results))
  }

  val EnoughResults = 10

  val FirstName = "givenName"
  val LastName = "sn"
  val UniversityId = "warwickUniId"
  val Usercode = "cn"

  private fun queryUsers(query: FlexiPickerQuery): List<User> {
    val words = query.query.trim().split(Regex("\\s+"))

    val results: MutableList<User> = mutableListOf()

    if (words.size == 2) {
      results.addAll(usersMatching(FirstName to words[0], LastName to words[1]))

      if (results.size < EnoughResults)
        results.addAll(usersMatching(LastName to words[0], FirstName to words[1]))
    } else if (words.size == 1) {
      val word = words.first()

      if (word.toCharArray().all { it.isDigit() })
        results.addAll(usersMatching(UniversityId to word))
      else
        results.addAll(usersMatching(LastName to word))

      if (results.size < EnoughResults)
        results.addAll(usersMatching(FirstName to word))

      if (results.size < EnoughResults)
        results.addAll(usersMatching(Usercode to word))
    }

    return results.sortedBy { it.fullName }
  }

  private fun usersMatching(vararg filter: Pair<String, String>): List<User> =
    userLookup.findUsersWithFilter(filter.toMap().filterValues { it.isNotBlank() }.mapValues { it.value.trim() + "*" })
}

interface FlexiPickerResult {
  val type: String
  val value: String
}

class UserFlexiPickerResult(user: User) : FlexiPickerResult {
  val name: String = user.fullName ?: "Unknown user"

  val department: String = user.department ?: "Unknown department"

  val userType: String? = user.extraProperties["urn:websignon:usertype"] ?: when {
    user.isStaff -> "Staff"
    user.isStudent -> "Student"
    user.isAlumni -> "Alumnus"
    else -> "Unknown type"
  }

  override val value: String = user.userId

  override val type: String = "user"
}

class GroupFlexiPickerResult(group: Group) : FlexiPickerResult {
  override val type: String = "group"

  override val value: String = group.name
}

data class FlexiPickerQuery(
  val query: String,
  val exact: Boolean = false,
  val includeUsers: Boolean = false,
  val includeGroups: Boolean = false,
  val universityId: Boolean = false
)
