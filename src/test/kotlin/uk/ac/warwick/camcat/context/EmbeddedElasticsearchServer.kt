package uk.ac.warwick.camcat.context

import org.springframework.context.Lifecycle
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic
import pl.allegro.tech.embeddedelasticsearch.PopularProperties.*
import java.util.concurrent.TimeUnit

@Component
@Profile("integrationTest")
class ElasticsearchBean : Lifecycle {
  override fun isRunning(): Boolean = EmbeddedElasticsearchServer.running

  final override fun start() {
    EmbeddedElasticsearchServer.start()
  }

  override fun stop() {
    EmbeddedElasticsearchServer.stop()
  }

  init {
    start()
  }
}

object EmbeddedElasticsearchServer {
  private val elastic = EmbeddedElastic.builder()
    .withElasticVersion("6.8.4")
    .withSetting(CLUSTER_NAME, "camcat")
    .withSetting(HTTP_PORT, 9201)
    .withSetting(TRANSPORT_TCP_PORT, 9301)
    .withCleanInstallationDirectoryOnStop(false)
    .withStartTimeout(1, TimeUnit.MINUTES)
    .build()

  var running = false
    private set

  @Synchronized
  fun start() {
    if (!running) {
      running = true
      elastic.start()
    }
  }

  @Synchronized
  fun stop() {
    if (running) {
      running = false
      elastic.stop()
    }
  }
}

