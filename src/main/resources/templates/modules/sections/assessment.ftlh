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
<table class="table table-bordered table-striped" id="module-assessment">
  <thead>
  <tr>
    <th>Assessment</th>
    <th>Weighting</th>
    <th>Study time</th>
  </tr>
  </thead>
  <tbody>
  <#list module.assessmentGroups as group>
    <tr>
    <#list group.components as component>
      <tr>
        <td>
          ${component.name!""}
        </td>
        <td>
          <#if component.weighting??>
            ${component.weighting}%
          </#if>
        </td>
        <td>
          ${component.studyTime!""}
        </td>
      </tr>
        <tr>
          <td colspan="3">
            <#if component.description??>
              ${renderMarkdown(component.description)}
            </#if>
          </td>
        </tr>
    </#list>
  </#list>
  </tbody>
</table>

<#if module.assessmentFeedback??>
  <h5>Feedback on assessment</h5>
  ${renderMarkdown(module.assessmentFeedback)}
</#if>

