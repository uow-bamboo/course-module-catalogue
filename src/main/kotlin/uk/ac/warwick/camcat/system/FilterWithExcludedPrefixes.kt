package uk.ac.warwick.camcat.system

import javax.servlet.*
import javax.servlet.http.HttpServletRequest

class FilterWithExcludedPrefixes<T : Filter>(private val underlying: T, private val excludedPrefixes: List<String>) :
  Filter {

  override fun init(filterConfig: FilterConfig?) = underlying.init(filterConfig)

  override fun destroy() = underlying.destroy()

  override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
    if (shouldRunFilter(request)) {
      underlying.doFilter(request, response, chain)
    } else {
      chain?.doFilter(request, response)
    }
  }

  private fun shouldRunFilter(request: ServletRequest?) =
    request is HttpServletRequest && excludedPrefixes.none { request.servletPath.startsWith(it) }
}
