package uk.ac.warwick.camcat.system

import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class Logging {
  val logger: Logger = LoggerFactory.getLogger(this.javaClass)
}
