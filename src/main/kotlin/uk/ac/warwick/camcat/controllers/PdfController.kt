package uk.ac.warwick.camcat.controllers

import org.springframework.http.HttpHeaders.CONTENT_DISPOSITION
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import uk.ac.warwick.camcat.services.PdfService
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/pdf")
class PdfController(
  private val pdfService: PdfService
) {
  @GetMapping("/test.pdf", produces = [MediaType.APPLICATION_PDF_VALUE])
  fun get(response: HttpServletResponse) {
    response.setHeader(CONTENT_DISPOSITION, "attachment; filename=test.pdf")

    pdfService.render("pdf/test", mapOf("name" to "Course and Module Catalogue"), response.outputStream)
  }
}
