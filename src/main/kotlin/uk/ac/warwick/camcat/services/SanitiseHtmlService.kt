package uk.ac.warwick.camcat.services

import com.google.common.base.Predicate
import org.owasp.html.ElementPolicy
import org.owasp.html.HtmlPolicyBuilder
import org.owasp.html.PolicyFactory
import org.springframework.stereotype.Service
import java.net.URI

interface SanitiseHtmlService {
  fun sanitise(html: String): String
  fun sanitiseTrusted(html: String): String
}

@Service
class OwaspSanitiseHtmlService : SanitiseHtmlService {
  override fun sanitise(html: String): String = standardSanitisePolicy.sanitize(html)
  override fun sanitiseTrusted(html: String): String = trustedSanitisePolicy.sanitize(html)

  object WarwickUrlPredicate : Predicate<String> {
    override fun apply(input: String?): Boolean {
      if (input == null) {
        return false
      }

      return try {
        val uri = URI(input)
        val host = uri.host
        (uri.isAbsolute && (host.endsWith(".warwick.ac.uk") || host == "warwick.ac.uk")) || !uri.isAbsolute
      } catch (e: Throwable) {
        false
      }
    }
  }

  private fun baseSanitisePolicyBuilder(): HtmlPolicyBuilder {
    return HtmlPolicyBuilder()
      .allowElements(OpenLinksInNewTabElementPolicy, "a")
      .allowUrlProtocols("https", "http")
      .allowAttributes("title").onElements("a")
      .allowCommonBlockElements()
      .allowCommonInlineFormattingElements()
      .disallowElements("script") // this should not be needed, but just being explicit here
  }

  private val trustedSanitisePolicy: PolicyFactory = baseSanitisePolicyBuilder()
    .allowAttributes("href").onElements("a")
    .toFactory()

  private val standardSanitisePolicy: PolicyFactory = baseSanitisePolicyBuilder()
    .allowAttributes("href").matching(WarwickUrlPredicate).onElements("a")
    .toFactory()

  object OpenLinksInNewTabElementPolicy : ElementPolicy {
    private fun upsertAttributeValue(key: String, value: String, attributes: MutableList<String>) {
      val index = attributes.indexOf(key)
      if (index < 0) {
        attributes.add(key)
        attributes.add(value)
      } else {
        attributes[index + 1] = value
      }
    }

    override fun apply(elementName: String, attrs: MutableList<String>): String? {
      upsertAttributeValue("target", "_blank", attrs)
      upsertAttributeValue("rel", "noopener", attrs)
      return elementName
    }
  }
}
