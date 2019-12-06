<#if module.teachingSplits?has_content>
  <h2>Teaching split</h2>
  <table class="table table-bordered" id="module-teaching-split">
    <thead>
    <tr>
      <th>Provider</th>
      <th>Weighting</th>
    </tr>
    </thead>
    <tbody>
    <#list module.teachingSplits as item>
      <tr>
        <td>${item.department.name}</td>
        <td>
          <#if item.weighting??>
            ${item.weighting}%
          </#if>
        </td>
      </tr>
    </#list>
    </tbody>
  </table>
</#if>

<#if module.studyAmounts?has_content>
  <h2>Study time</h2>
  <table class="table table-bordered" id="module-study-time">
    <thead>
    <tr>
      <th>Type</th>
      <th>Required</th>
      <th>Optional</th>
    </tr>
    </thead>
    <tbody>
    <#list module.studyAmounts as item>
      <tr>
        <td>${item.type}</td>
        <td>
          ${item.requiredDescription!""}
        </td>
        <td>
          ${item.optionalDescription!""}
        </td>
      </tr>
    </#list>
    </tbody>
    <tfoot>
    <tr>
      <td>Total</td>
      <td id="module-total-study-hours">${module.totalStudyHours} hours</td>
      <td></td>
    </tr>
    </tfoot>
  </table>
</#if>

<#if module.privateStudyDescription??>
  <h5>Private study description</h5>
  ${renderMarkdown(module.privateStudyDescription)}
</#if>

<#if module.otherActivityDescription??>
  <h5>Other activity description</h5>
  ${renderMarkdown(module.otherActivityDescription)}
</#if>

<#if module.costs?has_content>
  <h2>Costs</h2>
  <table class="table table-bordered" id="module-costs">
    <thead>
    <tr>
      <th>Category</th>
      <th>Description</th>
      <th>Funded by</th>
      <th>Cost to student</th>
    </tr>
    </thead>
    <tbody>
    <#list module.costs as cost>
      <tr>
        <td>${cost.category}</td>
        <td>
          <#if cost.description??>
            ${renderMarkdown(cost.description)}
          </#if>
        </td>
        <td>${cost.fundedBy!""}</td>
        <td>
          <#if cost.costToStudent??>
            ${cost.costToStudent?string.currency}
          </#if>
        </td>
      </tr>
    </#list>
    </tbody>
  </table>
</#if>
