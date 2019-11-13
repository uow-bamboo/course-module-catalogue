package uk.ac.warwick.camcat.system.context

import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class HttpContext {
  @Bean
  fun httpClient(): CloseableHttpClient = HttpClients.custom()
    .setUserAgent("Warwick Course and Module Catalogue")
    .build()
}
