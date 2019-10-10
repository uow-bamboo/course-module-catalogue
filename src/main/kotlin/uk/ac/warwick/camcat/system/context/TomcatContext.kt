package uk.ac.warwick.camcat.system.context

import org.apache.catalina.connector.Connector
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configures an additional Tomcat Connector listening over http if the property <code>server.http.port</code>
 * has been set. This port is normally only used for the /service endpoints because the F5 health monitors don't
 * like doing too much HTTPS.
 */
@Configuration
class TomcatContext {
  @Value("\${server.http.port:0}")
  private val httpPort: Int = 0

  @Bean
  fun servletContainer(): TomcatServletWebServerFactory {
    val tomcat = TomcatServletWebServerFactory()
    if (httpPort > 0) {
      val connector = Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL)
      connector.port = httpPort
      tomcat.addAdditionalTomcatConnectors(connector)
    }
    return tomcat
  }
}
