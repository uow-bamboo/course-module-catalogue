package uk.ac.warwick.camcat.presenters

import org.springframework.stereotype.Component
import uk.ac.warwick.userlookup.AnonymousUser
import uk.ac.warwick.userlookup.User
import uk.ac.warwick.userlookup.UserLookupInterface

@Component
class UserPresenterFactory(private val userLookup: UserLookupInterface) {
  fun buildFromPersonnelCode(personnelCode: String): UserPresenter = UserPresenter.build(
    if (personnelCode.length == 9) {
      val universityId = personnelCode.drop(2)

      userLookup.getUserByWarwickUniId(universityId) ?: AnonymousUser()
    } else AnonymousUser()
  )
}

data class UserPresenter(
  val universityId: String,
  val name: String?,
  val email: String?
) {
  companion object {
    fun build(user: User): UserPresenter = UserPresenter(
      universityId = user.warwickId,
      name = user.fullName,
      email = user.email
    )
  }
}

