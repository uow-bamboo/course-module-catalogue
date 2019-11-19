package uk.ac.warwick.camcat.presenters

import org.springframework.stereotype.Component
import uk.ac.warwick.userlookup.AnonymousUser
import uk.ac.warwick.userlookup.User
import uk.ac.warwick.userlookup.UserLookup
import uk.ac.warwick.userlookup.UserLookupInterface

@Component
class UserPresenterFactory(private val userLookup: UserLookupInterface) {
  fun buildFromPersonnelCode(personnelCode: String): UserPresenter = UserPresenter(
    if (personnelCode.length == 9) {
      val universityId = personnelCode.drop(2)

      userLookup.getUserByWarwickUniId(universityId)
    } else AnonymousUser()
  )
}

class UserPresenter(user: User) {
  val universityId = user.warwickId
  val name = user.fullName
  val email = user.email
}
