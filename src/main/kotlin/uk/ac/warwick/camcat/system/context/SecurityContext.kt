package uk.ac.warwick.camcat.system.context

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy
import uk.ac.warwick.camcat.system.security.WarwickAuthenticationDetails
import uk.ac.warwick.camcat.system.security.WarwickAuthenticationManager
import javax.inject.Named
import javax.servlet.Filter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
class SecurityContext(
  private val authenticationManager: WarwickAuthenticationManager,
  @Named("ssoClientFilter") private val ssoClientFilter: Filter
) : WebSecurityConfigurerAdapter() {

  val contentSecurityPolicy: String = listOf(
    "default-src 'none'", // Deny by default, require everything to be explicitly set
    "img-src 'self' data:", // Need data: to detect webp support in ID7
    "style-src 'self' https://fonts.googleapis.com 'report-sample'", // Enable reporting
    "font-src 'self' data: https://fonts.gstatic.com", // We need the data: here for a reason I can't remember
    "script-src 'self' 'report-sample'", // Enable reporting
    "frame-src 'self' https://websignon.warwick.ac.uk https://my.warwick.ac.uk",
    "connect-src 'self' https://my.warwick.ac.uk https://status.warwick.ac.uk",
    "object-src 'none'", // We might need 'self' for PDFs but it'd be better for PDFs to serve their own more locked-down CSP
    "form-action 'self'",
    "frame-ancestors 'none'",
    "manifest-src 'self'",
    "report-uri https://warwick.report-uri.com/r/d/csp/enforce"
  ).joinToString(separator = "; ")

  val featurePolicy: String = listOf(
    "accelerometer 'self' https://my.warwick.ac.uk",
    "camera 'none'",
    "geolocation 'none'",
    "gyroscope 'self' https://my.warwick.ac.uk",
    "magnetometer 'none'",
    "microphone 'none'",
    "payment 'none'",
    "usb 'none'"
  ).joinToString(separator = "; ")

  override fun configure(http: HttpSecurity?) {
    http
      ?.addFilter(preAuthenticatedProcessingFilter())
      ?.addFilterBefore(ssoClientFilter, preAuthenticatedProcessingFilter().javaClass)
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
      ?.cors {}
      ?.headers { config ->
        config.contentSecurityPolicy { it.policyDirectives(contentSecurityPolicy) }
        config.referrerPolicy { it.policy(ReferrerPolicy.NO_REFERRER_WHEN_DOWNGRADE) }
        config.featurePolicy(featurePolicy)
      }
  }

  @Bean
  fun preAuthenticatedProcessingFilter(): WarwickPreAuthenticatedProcessingFilter {
    val filter = WarwickPreAuthenticatedProcessingFilter()
    filter.setAuthenticationManager(authenticationManager)
    filter.setAuthenticationDetailsSource(::WarwickAuthenticationDetails)
    return filter
  }
}

