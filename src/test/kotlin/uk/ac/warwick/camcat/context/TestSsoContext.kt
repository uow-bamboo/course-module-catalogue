package uk.ac.warwick.camcat.context

import org.apache.commons.configuration.MapConfiguration
import org.mockito.Mockito.mock
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import uk.ac.warwick.sso.client.SSOConfiguration
import uk.ac.warwick.userlookup.GroupService
import uk.ac.warwick.userlookup.UserLookup
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

@Configuration
@Profile("integrationTest")
class TestSsoContext {
  @Bean
  fun ssoConfiguration(): SSOConfiguration =
    // Just enough configuration for the SSOLoginLinkGenerator
    SSOConfiguration(MapConfiguration(mapOf(
      "origin.login.location" to "https://websignon.warwick.ac.uk/origin/hs",
      "origin.logout.location" to "https://websignon.warwick.ac.uk/origin/logout",
      "shire.location" to "https://example.warwick.ac.uk/sso/acs",
      "shire.providerid" to "urn:example.warwick.ac.uk:camcat:service"
    )))

  @Bean("ssoClientFilter")
  fun nullFilter(): Filter {
    return Filter { request: ServletRequest, response: ServletResponse, chain: FilterChain ->
      chain.doFilter(request, response)
    }
  }

  @Bean("userLookup")
  fun userLookup(): UserLookup = mock(UserLookup::class.java)

  @Bean("groupService")
  fun groupService(): GroupService = mock(GroupService::class.java)
}
