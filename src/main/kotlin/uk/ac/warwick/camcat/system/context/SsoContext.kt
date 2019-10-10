package uk.ac.warwick.camcat.system.context

import org.springframework.boot.web.servlet.ServletContextInitializer
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import uk.ac.warwick.sso.client.LogoutServlet
import uk.ac.warwick.sso.client.SSOConfigLoader
import uk.ac.warwick.sso.client.SSOConfigLoader.SSO_CACHE_KEY
import uk.ac.warwick.sso.client.SSOConfigLoader.SSO_CONFIG_KEY
import uk.ac.warwick.sso.client.SSOConfiguration
import uk.ac.warwick.sso.client.ShireServlet
import uk.ac.warwick.sso.client.cache.UserCache
import uk.ac.warwick.sso.client.cache.spring.DatabaseUserCache
import uk.ac.warwick.userlookup.GroupService
import uk.ac.warwick.userlookup.UserLookup
import uk.ac.warwick.userlookup.webgroups.WarwickGroupsService
import javax.sql.DataSource

@Configuration
class SsoContext(private val dataSource: DataSource) {
  @Bean
  fun shireServlet(): ServletRegistrationBean<ShireServlet> =
    ServletRegistrationBean(ShireServlet(), "/sso/acs")

  @Bean
  fun logoutServlet(): ServletRegistrationBean<LogoutServlet> =
    ServletRegistrationBean(LogoutServlet(), "/sso/logout")

  @Bean
  fun userLookup(): UserLookup = UserLookup()

  @Bean
  fun groupService(): GroupService = WarwickGroupsService()

  @Bean
  fun initializer(): ServletContextInitializer =
    ServletContextInitializer {
      it.setAttribute(SSO_CONFIG_KEY, ssoConfiguration())
      it.setAttribute(SSO_CACHE_KEY, userCache())
    }

  @Bean
  @Profile("!test")
  fun ssoConfiguration(): SSOConfiguration {
    val config = SSOConfigLoader().loadSSOConfig("/sso-config.xml")
    config.addOverride("cluster.enabled", "true")
    return config
  }

  @Bean
  @Profile("!test")
  fun userCache(): UserCache {
    val cache = DatabaseUserCache(ssoConfiguration())
    cache.dataSource = dataSource
    return cache
  }
}

