<#if module.mustPassAllAssessmentComponents>
  <p>
    You must pass all assessment components to pass the module.
  </p>
<#else>
  <p>
    You do not need to pass all assessment components to pass the module.
  </p>
</#if>

<#if module.hasAuditOnlyAssessmentGroup>
  <p>
    Students can register for this module without taking any assessment.
  </p>
</#if>
<table class="table table-bordered" id="module-assessment">
  <thead>
  <tr>
    <th>Assessment</th>
    <th>Type</th>
    <th>Weighting</th>
  </tr>
  </thead>
  <tbody>
  <#list module.assessmentGroups as group>
    <tr>
    <#list group.components as component>
      <tr>
        <td>${component.name!""}</td>
        <td>${component.type!""}</td>
        <td>
          <#if component.weighting??>
            ${component.weighting}%
          </#if>
        </td>
      </tr>
      <#if component.description??>
        <tr>
          <td colspan="3">
            ${renderMarkdown(component.description)}
          </td>
        </tr>
      </#if>
    </#list>
  </#list>
  </tbody>
</table>

<#if module.assessmentFeedback??>
  <h5>Feedback on assessment</h5>
  ${renderMarkdown(module.assessmentFeedback)}
</#if>

