<#macro nav navigation>
    <#local path = navigation.path>
  <nav class="navbar navbar-primary <#if navigation.primaryActive>hidden-xs</#if>" role="navigation">
    <ul class="nav navbar-nav">
        <#list navigation.items as item>
            <#if item.dropdown && item.hasChildren>
              <li class="dropdown <#if item.isActive(path)>active</#if> <#if item.hasActiveChildren(path)>next-secondary</#if>">
                <a href="${item.route}" class="dropdown-toggle" data-toggle="dropdown" role="button"
                   aria-haspopup="true" aria-expanded="false">${item.label} <span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <#list item.children as child>
                      <li <#if child.isActive(path)>class="active"</#if>>
                        <a href="${child.route}">${child.label}</a>
                      </li>
                    </#list>
                </ul>
              </li>
            <#else>
              <li <#if item.isActive(path)>class="active <#if item.hasActiveChildren(path)>next-secondary</#if>"</#if>>
                <a href="${item.route}">${item.label}</a>
              </li>
            </#if>
        </#list>
    </ul>
  </nav>

    <#list navigation.secondary as item>
      <nav class="navbar navbar-secondary" role="navigation">
        <ul class="nav navbar-nav">
          <li class="nav-breadcrumb first <#if item.hasActiveChildren(path)>last active</#if>">
            <a href="${item.route}">${item.label}</a>
          </li>
            <#list item.activeChildren(path) as child>
                <@breadcrumb child path "second" />
            </#list>
        </ul>
      </nav>
    </#list>

    <#list navigation.tertiary as item>
      <nav class="navbar navbar-tertiary" role="navigation">
        <ul class="nav navbar-nav">
            <#list item.children as child>
              <li <#if child.isActive(path)>class="active"</#if>>
                <a href="${child.route}">${child.label}</a>
              </li>
            </#list>
        </ul>
      </nav>
    </#list>
</#macro>

<#macro breadcrumb item path extraClasses="">
    <#if item.hasActiveChildren(path)>
      <li class="nav-breadcrumb ${extraClasses}">
        <a href="${item.route}">${item.label}</a>
      </li>
        <#list item.activeChildren(path) as child>
            <@breadcrumb child path />
        </#list>
    <#else>
      <li class="nav-breadcrumb ${extraClasses} last active <#if item.hasChildren>next-tertiary</#if>">
        <a href="${item.route}">${item.label}</a>
      </li>
    </#if>
</#macro>
