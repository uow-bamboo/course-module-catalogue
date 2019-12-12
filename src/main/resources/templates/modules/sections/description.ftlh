<#if module.introductoryDescription??>
  <h5>Introductory description</h5>
  ${renderMarkdown(module.introductoryDescription)}
</#if>

<#if module.url??>
  <p>
    <a href="${module.url}" id="module-web-page" target="_blank">Module web page</a>
  </p>
</#if>

<#if module.aims??>
  <h5>Module aims</h5>
  <div id="module-aims">
    ${renderMarkdown(module.aims)}
  </div>
</#if>

<#if module.outlineSyllabus??>
  <h5>Outline syllabus</h5>
  ${renderMarkdown(module.outlineSyllabus)}
</#if>

<#if module.learningOutcomes??>
  <h5>Learning outcomes</h5>
  <ul>
    <#list module.learningOutcomes as outcome>
      <li>${stripBullets(outcome)}</li>
    </#list>
  </ul>
</#if>

<#if module.indicativeReadingList?? || module.readingListUrl??>
  <h5>Indicative reading list</h5>
  <#if module.indicativeReadingList??>
    ${renderMarkdown(module.indicativeReadingList)}
  </#if>
  <#if module.readingListUrl??>
    <p>
      <a href="${module.readingListUrl}" target="_blank">View reading list on Talis Aspire</a>
    </p>
  </#if>
</#if>

<#if module.researchElement??>
  <h5>Research element</h5>
  ${renderMarkdown(module.researchElement)}
</#if>

<#if module.interdisciplinary??>
  <h5>Interdisciplinary</h5>
  ${renderMarkdown(module.interdisciplinary)}
</#if>

<#if module.international??>
  <h5>International</h5>
  ${renderMarkdown(module.international)}
</#if>

<#if module.subjectSpecificSkills??>
  <h5>Subject specific skills</h5>
  ${renderMarkdown(module.subjectSpecificSkills)}
</#if>

<#if module.transferableSkills??>
  <h5>Transferable skills</h5>
  ${renderMarkdown(module.transferableSkills)}
</#if>
