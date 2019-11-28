<#import 'spring.ftl' as spring>
<#import 'helpers/nav.ftlh' as nav>

<#macro layout title="">
  <!DOCTYPE html>
  <html lang="en-GB" class="no-js">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="apple-touch-icon" sizes="180x180" href="<@spring.url "/assets/images/apple-touch-icon.png" />">
    <link rel="icon" type="image/png" sizes="32x32" href="<@spring.url "/assets/images/favicon-32x32.png" />">
    <link rel="icon" type="image/png" sizes="16x16" href="<@spring.url "/assets/images/favicon-16x16.png" />">
    <link rel="manifest" href="<@spring.url "/assets/images/site.webmanifest" />">
    <link rel="mask-icon" href="<@spring.url "/assets/images/safari-pinned-tab.svg" />" color="#303060">
    <link rel="shortcut icon" href="<@spring.url "/assets/images/favicon.ico" />">
    <meta name="msapplication-TileColor" content="#303060">
    <meta name="msapplication-config" content="<@spring.url "/assets/images/browserconfig.xml" />">
    <meta name="theme-color" content="#303060">

    <title><#if title?has_content>${title} - </#if>Course and Module Catalogue</title>

    <!-- Lato web font -->
    <link href="//fonts.googleapis.com/css?family=Lato:300,400,700,300italic,400italic,700italic&amp;subset=latin,latin-ext"
          rel="stylesheet" type="text/css">

    <!-- ID7 -->
    <link rel="stylesheet" href="<@spring.url "/assets/style.css" />">

    <#if _csrf??>
      <meta name="_csrf_header" content="X-XSRF-TOKEN">
      <meta name="_csrf" content="${_csrf.token}">
    </#if>
  </head>
  <body>
  <div class="id7-left-border"></div>
  <div class="id7-fixed-width-container">
    <a class="sr-only sr-only-focusable" href="#main">Skip to main content</a>
    <a class="sr-only sr-only-focusable" href="#primary-nav">Skip to navigation</a>

    <header class="id7-page-header">
        <#if requestContext?? && requestContext.masquerading>
          <div id="masquerade-notice" class="sysadmin-only-content">
            Masquerading as <strong>${requestContext.user.fullName}</strong>
            (${requestContext.user.userId}).
            <a href="<@spring.url '/masquerade' />">Change</a>
          </div>
        </#if>
      <div class="id7-utility-masthead">
        <nav class="id7-utility-bar">
            <#if requestContext??>
              <ul>
                  <#if requestContext.user.foundUser>
                    <li>
                      <a href="https://warwick.ac.uk/myaccount"
                         data-toggle="id7:account-popover"
                         data-name="${requestContext.user.fullName}"
                         data-loginlink="${requestContext.loginUrl}"
                         data-logoutlink="${requestContext.logoutUrl}">
                          ${requestContext.user.fullName}
                        <span class="caret"></span>
                      </a>
                    </li>
                  <#else>
                    <li>
                      <a href="${requestContext.loginUrl}">Sign in</a>
                    </li>
                  </#if>
              </ul>
            </#if>
        </nav>

        <div class="id7-masthead">
          <div class="id7-masthead-contents">
            <div class="clearfix">
              <div class="id7-logo-column">
                <div class="id7-logo-row">
                  <div class="id7-logo">
                    <a href="http://warwick.ac.uk" title="Warwick homepage">
                      <img src="<@spring.url '/assets/lib/id7/images/shim.gif' />" alt="Warwick">
                    </a>
                  </div>
                  <nav class="id7-site-links">
                    <ul>
                      <li><a href="https://warwick.ac.uk/study">Study</a></li>
                      <li><a href="https://warwick.ac.uk/research">Research</a></li>
                      <li><a href="https://warwick.ac.uk/business">Business</a></li>
                      <li><a href="https://warwick.ac.uk/alumni">Alumni</a></li>
                      <li><a href="https://warwick.ac.uk/news">News</a></li>
                      <li><a href="https://warwick.ac.uk/engagement">Engagement</a></li>
                    </ul>
                  </nav>
                </div>
              </div>
            </div>

            <div class="id7-header-text clearfix">
              <h1>
                <span class="id7-current-site-link">
                  <a href="<@spring.url '/' />">Course and Module Catalogue</a>
                </span>
              </h1>
            </div>
          </div>
        </div>
      </div>

        <#if navigation??>
          <div class="id7-navigation" id="primary-nav">
              <@nav.nav navigation />
          </div>
        </#if>
    </header>

    <main class="id7-main-content-area" id="main">
      <header class="id7-main-content-header">
        <div class="id7-horizontal-divider">
          <svg xmlns="http://www.w3.org/2000/svg" x="0" y="0" version="1.1" width="1130" height="40"
               viewBox="0, 0, 1130, 40">
            <path d="m 0,0.5 1030.48, 0 22.8,39.5 16.96,-31.4 16.96,31.4 22.8,-39.5 20,0" stroke="#383838"
                  fill="none" class="divider"></path>
          </svg>
        </div>
      </header>

      <div class="id7-main-content">
          <#nested>
      </div>
    </main>

    <footer class="id7-page-footer id7-footer-divider">
      <div class="id7-app-footer">
        <div class="id7-horizontal-divider">
          <svg xmlns="http://www.w3.org/2000/svg" x="0" y="0" version="1.1" width="1130" height="40"
               viewBox="0, 0, 1130, 40">
            <path d="m 0,0 1030.48, 0 22.8,40 16.96,-31.4 16.96,31.4 22.8,-40 20,0" class="divider" stroke="#383838"
                  fill="none"/>
          </svg>
        </div>
        <div class="id7-footer-utility">
          <ul>
            <li>Powered by <a href="/">Course and Module Catalogue</a></li>
            <li><a href="http://warwick.ac.uk/copyright">© ${copyrightYear}</a></li>
            <li><a href="http://warwick.ac.uk/terms">Terms</a></li>
            <li><a href="http://warwick.ac.uk/privacy">Privacy</a></li>
            <li><a href="http://warwick.ac.uk/cookies">Cookies</a></li>
            <li><a href="http://warwick.ac.uk/accessibility">Accessibility</a></li>
          </ul>
        </div>
      </div>
    </footer>
  </div>
  <div class="id7-right-border"></div>

  <script type="text/javascript" src="<@spring.url "/assets/lib/id7/js/id7-bundle.js" />"></script>
  <script type="text/javascript" src="<@spring.url "/assets/main.js" />"></script>
  </body>
  </html>
</#macro>

