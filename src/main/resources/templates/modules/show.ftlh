<#include '*/layout.ftlh'>

<@layout title="${module.code} ${module.title}">
  <h1>${module.code} ${module.title}</h1>

  <dl>
    <dt>Module code</dt>
    <dd id="module-code">${module.code}</dd>

    <dt>Module title</dt>
    <dd id="module-title">${module.title}</dd>

      <#if module.department??>
        <dt>Department</dt>
        <dd id="module-department-name">${module.department.name}</dd>
      </#if>

      <#if module.faculty??>
        <dt>Faculty</dt>
        <dd id="module-faculty-name">${module.faculty.name}</dd>
      </#if>

      <#if module.level??>
        <dt>Level</dt>
        <dd id="module-level-name">${module.level.name}</dd>
      </#if>

    <dt>Credit value</dt>
    <dd id="module-credit-value">${module.creditValue}</dd>

      <#if module.duration??>
        <dt>Module duration</dt>
        <dd>${module.duration}</dd>
      </#if>

    <dt>Study locations</dt>
    <dd>
      <ul class="list-unstyled">
          <#list module.locations as location>
            <li>
                ${location.name}
                <#if location.primary>
                  <span class="label label-primary">Primary</span>
                </#if>
            </li>
          </#list>
      </ul>
    </dd>
  </dl>

  <div class="clearfix"></div>

  <ul class="nav nav-tabs" role="tablist">
    <li role="presentation" class="active">
      <a href="#description" role="tab" data-toggle="tab" aria-controls="description">Description</a>
    </li>
    <li role="presentation">
      <a href="#study" role="tab" data-toggle="tab" aria-controls="study">Study</a>
    </li>
    <li role="presentation">
      <a href="#assessment" role="tab" data-toggle="tab" aria-controls="assessment">Assessment</a>
    </li>
    <li role="presentation">
      <a href="#availability" role="tab" data-toggle="tab" aria-controls="availability">Availability</a>
    </li>
  </ul>

  <div class="tab-content">
    <div id="description" role="tabpanel" class="tab-pane active">

        <#if module.introductoryDescription??>
          <h5>Introductory description</h5>
            ${renderMarkdown(module.introductoryDescription)}
        </#if>

        <#if module.url??>
          <p>
            <a href="${module.url}" id="module-web-page">Module web page</a>
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
                <a href="${module.readingListUrl}">View reading list on Talis Aspire</a>
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
    </div>

    <div id="availability" role="tabpanel" class="tab-pane">
      <h2>Related modules</h2>

      <h5>Pre-requisite modules</h5>
        <#if module.preRequisiteModules?has_content>
          <p>
            To take this module, you must have passed:
          </p>
          <ul id="module-pre-requisite-modules">
              <#list module.preRequisiteModules as req>
                <li>${req.code} ${req.title}</li>
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
                <li>${req.code} ${req.title}</li>
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
                <li>${req.code} ${req.title}</li>
              </#list>
          </ul>
        <#else>
          <p>
            There are no modules you can't take if you take this module.
          </p>
        </#if>

        <#--
        <h2>Courses</h2>
        <p>This module is available on the following courses:</p>

        <p>???</p>
        -->

      <h2>Occurrences</h2>
      <table class="table table-bordered" id="module-occurrences">
        <thead>
        <tr>
          <th>Location</th>
          <th>Module leader</th>
        </tr>
        </thead>
        <tbody>
        <#list module.occurrences as occurrence>
          <tr>
            <td>${occurrence.location.name}</td>
            <td>
                <#if occurrence.moduleLeader??>
                    ${occurrence.moduleLeader.name}
                <#else>
                  Unknown
                </#if>
            </td>
          </tr>
        </#list>
        </tbody>
      </table>

    </div>

    <div id="assessment" role="tabpanel" class="tab-pane">
        <#if module.mustPassAllAssessmentComponents>
          <p>
            You must pass all assessment components to pass the module.
          </p>
        <#else>
          <p>
            You do not need to pass all assessment components to pass the module.
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
            <th colspan="3">
                ${group.description} (${group.name})
                <#if group.default>
                  <span class="label label-primary">Default</span>
                </#if>
            </th>
          </tr>
            <#list group.components as component>
              <tr>
                <td>${component.name}</td>
                <td>${component.type}</td>
                <td>${component.weighting}%</td>
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

    </div>

    <div id="study" role="tabpanel" class="tab-pane">
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
                <td>${item.weighting}%</td>
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
                    ${renderMarkdown(cost.description)}
                </td>
                <td>${cost.fundedBy}</td>
                <td>${cost.costToStudent?string.currency}</td>
              </tr>
            </#list>
            </tbody>
          </table>
        </#if>
    </div>
  </div>
</@layout>