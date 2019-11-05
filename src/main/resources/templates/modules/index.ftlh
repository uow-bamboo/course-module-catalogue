<#include '*/layout.ftlh'>

<@layout title="Modules">
  <h1>Modules</h1>

  <form method="get">
    <div class="form-group">
        <@spring.bind "query.academicYear" />
      <label for="academicYear">Academic year</label>
      <select name="${spring.status.expression}" id="academicYear" class="form-control">
          <#list academicYears as academicYear>
            <option value="${academicYear.startYear?c}"
                    <#if (spring.status.value!"") == academicYear.startYear?c>selected</#if>
            >${academicYear.label}</option>
          </#list>
      </select>
    </div>

    <div class="form-group">
        <@spring.bind "query.code" />
      <label for="code">Module code</label>
      <input type="text" class="form-control" id="code" name="${spring.status.expression}"
             value="${spring.status.value!""}">
    </div>

    <div class="form-group">
        <@spring.bind "query.keywords" />
      <label for="keywords">Keywords</label>
      <input type="text" class="form-control" id="keywords" name="${spring.status.expression}"
             value="${spring.status.value!""}">
    </div>

    <div class="form-group">
        <@spring.bind "query.department" />
      <label for="department">Department</label>
      <select name="${spring.status.expression}" id="department" class="form-control">
        <option value=""></option>
          <#list departments as department>
            <option value="${department.code}"
                    <#if (spring.status.value!"") == department.code>selected</#if>
            >${department.name}</option>
          </#list>
      </select>
    </div>

    <div class="form-group">
        <@spring.bind "query.faculty" />
      <label for="faculty">Faculty</label>
      <select name="${spring.status.expression}" id="faculty" class="form-control">
        <option value=""></option>
          <#list faculties as faculty>
            <option value="${faculty.code}"
                    <#if (spring.status.value!"") == faculty.code>selected</#if>
            >${faculty.name}</option>
          </#list>
      </select>
    </div>

    <div class="form-group">
        <@spring.bind "query.level" />
      <label for="level">Level</label>
      <select name="${spring.status.expression}" id="level" class="form-control">
        <option value=""></option>
          <#list levels as level>
            <option value="${level.code}"
                    <#if (spring.status.value!"") == level.code>selected</#if>
            >${level.name}</option>
          </#list>
      </select>
    </div>

    <div class="form-group">
        <@spring.bind "query.leader" />
      <label for="leader">Module leader</label>
      <input type="text" class="form-control flexi-picker" data-include-groups="false" id="leader"
             name="${spring.status.expression}"
             value="${spring.status.value!""}">
    </div>

    <div class="form-group">
        <@spring.bind "query.creditValue" />
      <label for="creditValue">Credit value</label>
      <input type="number" min="0" step="0.5" class="form-control" id="creditValue" name="${spring.status.expression}"
             value="${spring.status.value!""}">
    </div>

    <div class="form-group">
        <@spring.bind "query.assessmentTypes" />
      <label>Assessment types</label>
        <#list assessmentTypes as assessmentType>
          <div class="checkbox">
            <label>
              <input type="checkbox" name="${spring.status.expression}" value="${assessmentType.code}"
                     <#if query.assessmentTypes?? && query.assessmentTypes?seq_contains(assessmentType.code)>checked</#if>
              >
                ${assessmentType.name}
            </label>
          </div>
        </#list>
    </div>

    <div class="form-group">
      <button type="submit" class="btn btn-primary">
        Search
      </button>
    </div>
  </form>
</@layout>
