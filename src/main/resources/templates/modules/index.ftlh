<#include '*/layout.ftlh'>

<@layout title="Modules">
  <form method="get" name="modules" class="modules-search-form">
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
            <@spring.bind "query.departments" />
            <label for="departments" class="sr-only">Department</label>
            <select name="${spring.status.expression}" id="departments" class="form-control multiselect department hidden"
                    multiple="multiple">
              <#list results.filterOptions.faculties as faculty>
                <#assign facultyDepartments=results.filterOptions.departments?filter(d -> d.faculty == faculty) />
                <optgroup label="${faculty.name!""}">
                  <#list facultyDepartments as department>
                    <option value="${department.code}"
                            <#if query.departments?seq_contains(department.code)>selected</#if>
                    >${department.shortName}</option>
                  </#list>
                </optgroup>
              </#list>
            </select>
          </div>

          <div class="col-sm-3">
            <@spring.bind "query.levels" />
            <label for="levels" class="sr-only">Level</label>
            <select name="${spring.status.expression}" id="levels" class="form-control multiselect level hidden"
                    multiple="multiple">
              <#list results.filterOptions.levels as level>
                <option value="${level.code}"
                        <#if query.levels?seq_contains(level.code)>selected</#if>
                >${level.name}</option>
              </#list>
            </select>
          </div>

          <div class="col-sm-3">
            <@spring.bind "query.creditValues" />
            <label for="creditValues" class="sr-only">Credit value</label>
            <select name="${spring.status.expression}" id="creditValues" class="form-control multiselect creditValue hidden"
                    multiple="multiple">
              <#list results.filterOptions.creditValues as creditValue>
                <option value="${creditValue}"
                        <#if query.creditValues?seq_contains(creditValue)>selected</#if>
                >${creditValue}</option>
              </#list>
            </select>
          </div>

          <div class="col-sm-3">
            <@spring.bind "query.assessmentTypes" />
            <label for="assessmentTypes" class="sr-only">Assessment type</label>
            <select name="${spring.status.expression}" id="assessmentTypes" class="form-control multiselect assessmentType hidden"
                    multiple="multiple">
              <#list results.filterOptions.assessmentTypes as assessmentType>
                <option value="${assessmentType.code}"
                        <#if query.assessmentTypes?seq_contains(assessmentType.code)>selected</#if>
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
