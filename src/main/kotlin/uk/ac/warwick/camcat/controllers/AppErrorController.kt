package uk.ac.warwick.camcat.controllers

import org.springframework.boot.web.servlet.error.ErrorAttributes
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.security.core.Authentication
import org.springframework.security.web.WebAttributes
import org.springframework.security.web.csrf.CsrfException
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.ModelAndView
import uk.ac.warwick.camcat.system.ServiceException
import uk.ac.warwick.camcat.system.security.Authority
import uk.ac.warwick.camcat.system.security.WarwickAuthentication
import javax.servlet.RequestDispatcher

@Controller
class AppErrorController(
  private val errorAttributes: ErrorAttributes
) : ErrorController {
  @RequestMapping("/error")
  fun error(
    request: WebRequest,
    auth: Authentication?
  ): ModelAndView {
    if (request.getAttribute(WebAttributes.ACCESS_DENIED_403, RequestAttributes.SCOPE_REQUEST) is CsrfException) {
      return ModelAndView("errors/csrf")
    }

    val throwable = errorAttributes.getError(request)

    if (throwable is ServiceException) {
      return ModelAndView("errors/500", mapOf("error" to throwable.message))
    }

    if (throwable is ModuleNotFoundResponseStatusException) {
      return ModelAndView("errors/moduleNotFound404", mapOf(
        "moduleCode" to throwable.moduleCode,
        "academicYear" to throwable.academicYear
      ))
    }

    val warwickAuth = auth as? WarwickAuthentication

    return when (request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE, RequestAttributes.SCOPE_REQUEST)) {
      403 -> ModelAndView(
        "errors/403",
        mapOf(
          "identity" to warwickAuth?.user?.firstName,
          "actualIdentity" to warwickAuth?.actualUser?.firstName
        ).filterValues { !it.isNullOrBlank() }
      )
      404 -> ModelAndView("errors/404")
      400 -> ModelAndView("errors/400")
      else -> ModelAndView(
        "errors/500",
        // TODO In Kotlin land this will always be a UndeclaredThrowableException with the cause being the actual thing, maybe we could teach ErrorAttributes to strip that out
        errorAttributes.getErrorAttributes(request, auth?.authorities?.contains(Authority.sysadmin) == true)
      )
    }
  }

  override fun getErrorPath(): String = "/error"
}
