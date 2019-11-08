package uk.ac.warwick.camcat.services

import org.junit.Assert.assertEquals
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.ac.warwick.camcat.context.ContextTest
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets

class PdfServiceTest : ContextTest() {
  @Autowired
  lateinit var service: PdfService

  @Test
  fun renders() {
    val html = "<h1>This is a PDF</h1>"

    val outputStream = ByteArrayOutputStream()
    service.write(html, outputStream)

    val signature = outputStream.toByteArray().take(5).toByteArray().toString(StandardCharsets.ISO_8859_1)

    assertEquals("%PDF-", signature)
  }
}
