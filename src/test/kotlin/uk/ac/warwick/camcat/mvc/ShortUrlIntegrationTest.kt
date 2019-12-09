package uk.ac.warwick.camcat.mvc

import org.junit.Test
import org.springframework.test.web.servlet.get

class ShortUrlIntegrationTest : IntegrationTest() {
  @Test
  fun moduleByStemCode() {
    mvc.get("/cs126").andExpect { redirectedUrl("/modules/2020/CS126-15") }
    mvc.get("/CS126").andExpect { redirectedUrl("/modules/2020/CS126-15") }
  }

  @Test
  fun moduleByCode() {
    mvc.get("/cs126-15").andExpect { redirectedUrl("/modules/2020/CS126-15") }
    mvc.get("/CS126-15").andExpect { redirectedUrl("/modules/2020/CS126-15") }
  }

  @Test
  fun moduleByAcademicYearAndStemCode() {
    mvc.get("/20/cs126").andExpect { redirectedUrl("/modules/2020/CS126-15") }
    mvc.get("/20/CS126").andExpect { redirectedUrl("/modules/2020/CS126-15") }
    mvc.get("/2020/CS126").andExpect { redirectedUrl("/modules/2020/CS126-15") }
  }

  @Test
  fun moduleByAcademicYearAndCode() {
    mvc.get("/20/cs126-15").andExpect { redirectedUrl("/modules/2020/CS126-15") }
    mvc.get("/20/CS126-15").andExpect { redirectedUrl("/modules/2020/CS126-15") }
    mvc.get("/2020/CS126-15").andExpect { redirectedUrl("/modules/2020/CS126-15") }
  }

  @Test
  fun notFoundModuleByStemCode() {
    mvc.get("/CS123").andExpect { status { isNotFound } }
  }

  @Test
  fun notFoundModuleByAcademicYearAndStemCode() {
    mvc.get("/20/CS123").andExpect { status { isNotFound } }
    mvc.get("/2020/CS123").andExpect { status { isNotFound } }
  }
}
