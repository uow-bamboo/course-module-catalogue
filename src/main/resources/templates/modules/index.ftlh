<#include '*/layout.ftlh'>

<@layout title="Modules">
  <form method="get" name="modules">
    <div class="form-group">
      <@spring.bind "query.keywords" />
      <label for="keywords" class="sr-only">Search for modules</label>
      <div class="input-group">
        <input type="search" class="form-control input-lg" autocomplete="off" autocapitalize="off" id="keywords"
               name="${spring.status.expression}" placeholder="Search for modules" value="${spring.status.value!""}">
        <div class="input-group-btn">
          <button type="submit" class="btn btn-lg btn-primary">
            Search
          </button>
        </div>
      </div>
    </div>

    <ul class="nav nav-tabs" role="tablist">
      <li role="presentation" class="active">
        <a href="#" role="tab">Modules</a>
      </li>
      <li role="presentation" class="disabled">
        <a href="#" role="tab">Courses</a>
      </li>
    </ul>

    <div class="tab-content">

      <div class="form-horizontal">
        <div class="form-group">
          <div class="col-sm-3">
            <@spring.bind "query.department" />
            <label for="department" class="sr-only">Department</label>
            <select name="${spring.status.expression}" id="department" class="form-control">
              <option value="">Any department</option>
              <#list results.filterOptions.faculties as faculty>
                <optgroup label="${faculty.name!""}">
                  <option value="${faculty.code}"
                          <#if (spring.status.value!"") == faculty.code>selected</#if>
                  >Any department in ${faculty.name}</option>
                  <#list results.filterOptions.departments as department>
                    <#if department.faculty == faculty>
                      <option value="${department.code}"
                              <#if (spring.status.value!"") == department.code>selected</#if>
                      >${department.name}</option>
                    </#if>
                  </#list>
                </optgroup>
              </#list>
            </select>
          </div>

          <div class="col-sm-3">
            <@spring.bind "query.level" />
            <label for="level" class="sr-only">Level</label>
            <select name="${spring.status.expression}" id="level" class="form-control">
              <option value="">Any level</option>
              <#list results.filterOptions.levels as level>
                <option value="${level.code}"
                        <#if (spring.status.value!"") == level.code>selected</#if>
                >${level.name}</option>
              </#list>
            </select>
          </div>

          <div class="col-sm-3">
            <@spring.bind "query.creditValue" />
            <label for="creditValue" class="sr-only">Credit value</label>
            <select name="${spring.status.expression}" id="creditValue" class="form-control">
              <option value="">Any credit value</option>
              <#list results.filterOptions.creditValues as creditValue>
                <option value="${creditValue}"
                        <#if spring.status.value?? && spring.status.value == creditValue?c>selected</#if>
                >${creditValue}</option>
              </#list>
            </select>
          </div>

          <div class="col-sm-3">
            <@spring.bind "query.assessmentType" />
            <label for="assessmentType" class="sr-only">Assessment type</label>
            <select name="${spring.status.expression}" id="assessmentType" class="form-control">
              <option value="">Any assessment type</option>
              <#list results.filterOptions.assessmentTypes as assessmentType>
                <option value="${assessmentType.code}"
                        <#if spring.status.value?? && spring.status.value == assessmentType.code>selected</#if>
                >${assessmentType.name}</option>
              </#list>
            </select>
          </div>
        </div>
      </div>
      <#if results?? && results?has_content>
        <#if results.lastPage gt 0>
          <nav aria-label="Pagination buttons" class="navbar">
            <#if results.lastPage gt 0>
              <nav aria-label="Pagination buttons">
                <ul class="pagination" data-current-page="${results.currentPage}">
                  <li>
                    <#if results.currentPage gt 0>
                      <button class="btn button" aria-label="Previous" value="${results.currentPage - 1}">
                        <span>&laquo;</span>
                      </button>
                    <#else>
                      <button class="btn button disabled" disabled>
                        <span aria-hidden="true">&laquo;</span>
                      </button>
                    </#if>
                  </li>
                  <#list results.pageRange as page>
                    <li>
                      <#if page == results.currentPage>
                        <button class="btn active" aria-label="${page + 1}" name="page" value="${page}">
                          ${page + 1}
                        </button>
                      <#else>
                        <button class="btn" name="page" aria-label="${page + 1}" value="${page}">
                          ${page + 1}
                        </button>
                      </#if>
                    </li>
                  </#list>
                  <li>
                    <#if results.currentPage != results.lastPage>
                      <button class="btn button" aria-label="Next" value="${results.currentPage + 1}">
                        <span>&raquo;</span>
                      </button>
                    <#else>
                      <button class="btn button disabled" disabled><span aria-hidden="true">&raquo;</span></button>
                    </#if>
                  </li>
                </ul>
              </nav>
            </#if>
          </nav>
        </#if>
        <table class="table table-striped">
          <thead>
          <tr>
            <th colspan="2">Module</th>
            <th>Dept</th>
          </tr>
          </thead>
          <tbody>
          <#list results.modules as module>
            <tr class="module">
              <td>
                <a href="/modules/${query.academicYear.startYear?c}/${module.code}">${module.code}</a>
              </td>
              <td>
                <a href="/modules/${query.academicYear.startYear?c}/${module.code}">${module.title}</a>
              </td>
              <td>
                <#if module.departmentCode??>
                  ${module.departmentShortName!(module.departmentCode)}
                </#if>
              </td>
            </tr>
          </#list>
          </tbody>
        </table>
      <#else>
        <p>There are no results.</p>
      </#if>
    </div>
  </form>
</@layout>
