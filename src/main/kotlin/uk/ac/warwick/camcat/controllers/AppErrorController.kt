package uk.ac.warwick.camcat.controllers

import org.apache.commons.lang3.exception.ExceptionUtils
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.security.web.WebAttributes
import org.springframework.security.web.csrf.CsrfException
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import uk.ac.warwick.camcat.system.ServiceException
import uk.ac.warwick.camcat.system.security.Authority
import uk.ac.warwick.camcat.system.security.WarwickAuthentication
import javax.servlet.RequestDispatcher
import javax.servlet.http.HttpServletRequest

@Controller
class AppErrorController : ErrorController {
  @RequestMapping("/error")
  fun error(
    request: HttpServletRequest,
    exception: Exception,
    auth: WarwickAuthentication?
  ): ModelAndView {
    if (request.getAttribute(WebAttributes.ACCESS_DENIED_403) is CsrfException) {
      return ModelAndView("errors/csrf")
    }

    if (exception is ServiceException) {
      return ModelAndView("errors/500", mapOf("message" to exception.message))
    }

    return when (request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)) {
      403 -> ModelAndView(
        "errors/403",
        mapOf(
          "identity" to auth?.user?.firstName,
          "actualIdentity" to auth?.actualUser?.firstName
        ).filterValues { !it.isNullOrBlank() }
      )
      404 -> ModelAndView("errors/404")
      400 -> ModelAndView("errors/400")
      else -> ModelAndView(
        "errors/500",
        if (auth?.authorities?.contains(Authority.sysadmin) == true)
          mapOf("stackTrace" to ExceptionUtils.getStackTrace(exception))
        else mapOf()
      )
    }
  }

  override fun getErrorPath(): String = "/error"
}
