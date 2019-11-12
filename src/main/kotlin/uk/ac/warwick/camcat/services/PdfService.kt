package uk.ac.warwick.camcat.services

import com.itextpdf.html2pdf.ConverterProperties
import com.itextpdf.html2pdf.HtmlConverter
import org.springframework.stereotype.Service
import java.io.OutputStream

interface PdfService {
  fun render(template: String, model: Any, outputStream: OutputStream)

  fun write(html: String, outputStream: OutputStream)
}

@Service
class FreeMarkerPdfService(
  private val templateRenderingService: TemplateRenderingService
) : PdfService {
  private val converterProperties = ConverterProperties()

  override fun render(template: String, model: Any, outputStream: OutputStream) {
    write(templateRenderingService.render(template, model), outputStream)
  }

  override fun write(html: String, outputStream: OutputStream) {
    HtmlConverter.convertToPdf(
      html,
      outputStream,
      converterProperties
    )
  }
}
