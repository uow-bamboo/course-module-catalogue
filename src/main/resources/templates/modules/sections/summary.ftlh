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
    <dd id="module-faculty-name">${module.faculty.name!(module.faculty.code)}</dd>
  </#if>

  <#if module.level??>
    <dt>Level</dt>
    <dd id="module-level-name">${module.level.name!(module.level.code)}</dd>
  </#if>

  <#if module.leader??>
    <dt>Module leader</dt>
    <dd id="module-leader">${module.leader.name!""}</dd>
  </#if>

  <dt>Credit value</dt>
  <dd id="module-credit-value">${module.creditValue}</dd>

  <#if module.duration??>
    <dt>Module duration</dt>
    <dd id="module-duration-value">${module.duration}</dd>
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
