package uk.ac.warwick.camcat.system.context

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.preauth.RequestAttributeAuthenticationFilter
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import uk.ac.warwick.camcat.system.security.WarwickAuthenticationDetails
import uk.ac.warwick.camcat.system.security.WarwickAuthenticationManager
import uk.ac.warwick.sso.client.SSOClientFilter
import uk.ac.warwick.userlookup.UserLookup
import uk.ac.warwick.userlookup.UserLookupInterface
import javax.inject.Named
import javax.servlet.Filter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
class SecurityContext(
  private val userLookup: UserLookupInterface,
  private val authenticationManager: WarwickAuthenticationManager,
  @Named("ssoClientFilter") private val ssoClientFilter: Filter
) : WebSecurityConfigurerAdapter() {
  override fun configure(http: HttpSecurity?) {
    http
      ?.addFilter(requestAttributeAuthenticationFilter())
      ?.addFilterBefore(ssoClientFilter, requestAttributeAuthenticationFilter().javaClass)
      ?.sessionManagement {
        it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      }
      ?.csrf {
        it
          .csrfTokenRepository(CookieCsrfTokenRepository())
          .sessionAuthenticationStrategy(NullAuthenticatedSessionStrategy())
          .ignoringAntMatchers("/sso/acs", "/sso/logout")
      }
      ?.authorizeRequests {
        it.anyRequest().permitAll()
      }
      ?.exceptionHandling {
        it.accessDeniedPage("/error")
      }
  }

  @Bean
  fun requestAttributeAuthenticationFilter(): RequestAttributeAuthenticationFilter {
    val filter = RequestAttributeAuthenticationFilter()
    filter.setPrincipalEnvironmentVariable("${SSOClientFilter.USER_KEY}_usercode")
    filter.setExceptionIfVariableMissing(false)
    filter.setAuthenticationManager(authenticationManager)
    filter.setAuthenticationDetailsSource(::WarwickAuthenticationDetails)
    return filter
  }
}

