package uk.ac.warwick.camcat.system

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Statistic
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.boot.actuate.health.HealthIndicatorRegistry
import org.springframework.boot.actuate.health.Status
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Controller
@RequestMapping("/service")
class ServiceController(
  private val healthIndicatorRegistry: HealthIndicatorRegistry,
  private val meterRegistry: MeterRegistry
) {
  private val healthIndicator: HealthIndicator = healthIndicatorRegistry.get("db")

  @RequestMapping("/gtg", produces = [MediaType.TEXT_PLAIN_VALUE])
  fun gtg() =
    if (healthIndicator.health().status == Status.UP) {
      ResponseEntity.ok("\"OK\"")
    } else {
      ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
        .body(healthIndicator.health().status.code)
    }

  @RequestMapping("/healthcheck", produces = [MediaType.APPLICATION_JSON_VALUE])
  fun healthcheck() =
    ResponseEntity.ok(mapOf(
      "success" to true,
      "data" to healthIndicatorRegistry.all.map { entry ->
        val health = entry.value.health()

        mapOf(
          "name" to entry.key,
          "status" to when (health.status) {
            Status.UP -> "okay"
            Status.DOWN -> "error"
            Status.OUT_OF_SERVICE -> "warning"
            else -> "unknown"
          },
          "message" to health.details.toString(),
          "testedAt" to ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME)
        )
      }
    ))

  @RequestMapping("/metrics", produces = [MediaType.APPLICATION_JSON_VALUE])
  fun metrics() =
    ResponseEntity.ok(mapOf(
      "success" to true,
      "data" to meterRegistry.meters.map { meter ->
        val measurements = meter.measure()
        val current = measurements.find { it.statistic == Statistic.VALUE }
        val total = measurements.find { it.statistic == Statistic.COUNT || it.statistic == Statistic.TOTAL }

        mapOf(
          "name" to meter.id.name,
          "current" to current?.value,
          "total" to total?.value
        ).filterValues { it != null }
      }
    ))
}
