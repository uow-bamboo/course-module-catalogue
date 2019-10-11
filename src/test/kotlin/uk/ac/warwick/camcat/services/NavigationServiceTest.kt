package uk.ac.warwick.camcat.services

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import uk.ac.warwick.camcat.system.security.Role

@RunWith(SpringRunner::class)
@ActiveProfiles("test")
@SpringBootTest
class NavigationServiceTest {
  @Autowired
  private lateinit var navigationService: NavigationService

  private fun auth(): Authentication? = SecurityContextHolder.getContext().authentication

  private fun nav() = navigationService.navigation(auth())

  @Test
  @WithAnonymousUser
  fun testAnonymous() = assertEquals(emptyList<NavigationItem>(), nav())

  @Test
  @WithMockUser
  fun testUser() = assertEquals(listOf(NavigationPage("Home", "http://localhost/")), nav())

  @Test
  @WithMockUser(roles = [Role.user, Role.masquerader])
  fun testMasquerader() = assertEquals(listOf("Home", "Masquerade"), nav().map { it.label })

  @Test
  @WithMockUser(roles = [Role.user, Role.sysadmin])
  fun testSysadmin() = assertEquals(listOf("Home", "Sysadmin"), nav().map { it.label })
}
