package uk.ac.warwick.camcat.services

import org.springframework.stereotype.Service
import uk.ac.warwick.userlookup.Group
import uk.ac.warwick.userlookup.webgroups.GroupInfo
import uk.ac.warwick.userlookup.webgroups.GroupNotFoundException

interface GroupService {
  fun getGroupsForUser(usercode: String): List<Group>
  fun getGroupsNamesForUser(usercode: String): List<String>
  fun isUserInGroup(usercode: String, group: String): Boolean
  fun getUserCodesInGroup(group: String): List<String>
  fun getRelatedGroups(group: String): List<Group>
  fun getGroupByName(name: String): Group?
  fun getGroupsForDeptCode(deptCode: String): List<Group>
  fun getGroupsForQuery(search: String): List<Group>
  fun getGroupInfo(name: String): GroupInfo?
}

@Service
class GroupServiceImpl(private val underlying: uk.ac.warwick.userlookup.GroupService) : GroupService {
  override fun getGroupsForUser(usercode: String): List<Group> =
    underlying.getGroupsForUser(usercode)

  override fun getGroupsNamesForUser(usercode: String): List<String> =
    underlying.getGroupsNamesForUser(usercode)

  override fun isUserInGroup(usercode: String, group: String): Boolean =
    underlying.isUserInGroup(usercode, group)

  override fun getUserCodesInGroup(group: String): List<String> =
    underlying.getUserCodesInGroup(group)

  override fun getRelatedGroups(group: String): List<Group> =
    underlying.getRelatedGroups(group)

  override fun getGroupByName(name: String): Group? =
    try {
      underlying.getGroupByName(name)
    } catch (e: GroupNotFoundException) {
      null
    }

  override fun getGroupsForDeptCode(deptCode: String): List<Group> =
    underlying.getGroupsForDeptCode(deptCode)

  override fun getGroupsForQuery(search: String): List<Group> =
    underlying.getGroupsForQuery(search)

  override fun getGroupInfo(name: String): GroupInfo? =
    try {
      underlying.getGroupInfo(name)
    } catch (e: GroupNotFoundException) {
      null
    }
}
