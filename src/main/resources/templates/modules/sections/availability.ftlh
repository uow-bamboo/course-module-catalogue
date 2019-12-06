<h2>Related modules</h2>

<h5>Pre-requisite modules</h5>
<#if module.preRequisiteModules?has_content>
  <p>
    To take this module, you must have passed:
  </p>
  <ul id="module-pre-requisite-modules">
    <#list module.preRequisiteModules as req>
      <li>${req.code} ${req.title!""}</li>
    </#list>
  </ul>
<#else>
  <p>
    There are no modules you need to have passed to take this module.
  </p>
</#if>
<h5>Post-requisite modules</h5>
<#if module.postRequisiteModules?has_content>
  <p>
    If you pass this module, you can take:
  </p>
  <ul id="module-post-requisite-modules">
    <#list module.postRequisiteModules as req>
      <li>${req.code} ${req.title!""}</li>
    </#list>
  </ul>
<#else>
  <p>
    There are no modules you can only take if you pass this module.
  </p>
</#if>
<h5>Anti-requisite modules</h5>
<#if module.antiRequisiteModules?has_content>
  <p>
    If you take this module, you cannot also take:
  </p>
  <ul id="module-anti-requisite-modules">
    <#list module.antiRequisiteModules as req>
      <li>${req.code} ${req.title!""}</li>
    </#list>
  </ul>
<#else>
  <p>
    There are no modules you can't take if you take this module.
  </p>
</#if>

<#if module.coreAvailability?has_content || module.optionalAvailability?has_content>
  <h2>Courses</h2>

  <#macro moduleAvailability list>
    <ul>
      <#list list as availability>
        <li>
          <#if availability.routes?size == 1>
            <#local route = availability.routes[0] />
            <#if route.year??>
              Year ${route.year} of
            <#elseif route.block??>
              Block ${route.block} of
            </#if>
          </#if>
          <a href="/courses/${academicYear.startYear?c}/${availability.courseCode}">
            ${availability.courseCode}
            ${availability.courseName!""}
          </a>
          <#if availability.routes?size gt 1>
            <ul>
              <#list availability.routes as route>
                <li>
                  <#if route.year??>
                    Year ${route.year} of
                  <#elseif route.block??>
                    Block ${route.block} of
                  </#if>
                  ${route.routeCode} ${route.routeName!""}
                </li>
              </#list>
            </ul>
          </#if>
        </li>
      </#list>
    </ul>
  </#macro>

  <#if module.coreAvailability?has_content>
    <p>This module is core for:</p>
    <div id="module-core-availability">
      <@moduleAvailability module.coreAvailability />
    </div>
  </#if>

  <#if module.optionalAvailability?has_content>
    <p>This module is optional for:</p>
    <div id="module-optional-availability">
      <@moduleAvailability module.optionalAvailability />
    </div>
  </#if>
</#if>
