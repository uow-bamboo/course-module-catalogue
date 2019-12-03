package uk.ac.warwick.camcat.services

import com.google.common.base.Predicate
import com.vladsch.flexmark.ext.autolink.AutolinkExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import org.owasp.html.ElementPolicy
import org.owasp.html.HtmlPolicyBuilder
import org.owasp.html.PolicyFactory
import org.springframework.stereotype.Service
import uk.ac.warwick.camcat.helpers.StripBullets.replaceBullets
import java.net.URI

interface MarkdownService {
  fun render(markdown: String): String
  fun renderTrustedValue(markdown: String): String
}

@Service
class CommonMarkMarkdownService : MarkdownService {
  private val parser = Parser.builder().extensions(mutableListOf(AutolinkExtension.create())).build()
  private val renderer = HtmlRenderer.builder().build()

  object WarwickUrlPredicate : Predicate<String> {
    override fun apply(input: String?): Boolean {
      return try {
        val uri: URI = URI(input)
        val host: String = uri.getHost()
        (uri.isAbsolute && (host.endsWith(".warwick.ac.uk") || host == "warwick.ac.uk")) || !uri.isAbsolute
      } catch (e: Throwable) {
        false
      }
    }
  }

  private fun renderUnsafe(markdown: String): String = renderer.render(parser.parse(replaceBullets(markdown, with = "-")))

  override fun render(markdown: String): String = standardSanitisePolicy.sanitize(renderUnsafe(markdown))
  override fun renderTrustedValue(markdown: String): String = trustedSanitisePolicy.sanitize(renderUnsafe(markdown))

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
    fun upsertAttributeValue(key: String, value: String, attributes: MutableList<String>): Unit {
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
