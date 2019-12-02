<#include '*/layout.ftlh'>

<@layout title="Home">
  <h1>Home</h1>

  <p>
    A module is lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque doloribus, earum ex facere itaque
    iure nihil nostrum omnis perferendis quibusdam quo repellat sit, soluta suscipit tempore! Itaque iure quibusdam rem?
  </p>

  <form method="get" action="/modules" name="modules">
    <div class="form-group">
      <label for="keywords" class="sr-only">Search for modules</label>
      <div class="input-group">
        <input type="search" class="form-control input-lg" autocomplete="off" autocapitalize="off" id="keywords"
               name="keywords" placeholder="Search for modules">
        <div class="input-group-btn">
          <button type="submit" class="btn btn-lg btn-primary">
            Search
          </button>
        </div>
      </div>
    </div>
  </form>

  <p>
    A course is lorem ipsum dolor sit amet, consectetur adipisicing elit. Excepturi illo minus modi nam officia
    provident reiciendis. Accusamus aliquam blanditiis et neque temporibus. Accusantium autem non officiis, possimus quo
    quos unde.
  </p>

  <form method="get" action="/courses" name="courses">
    <div class="form-group">
      <label for="keywords" class="sr-only">Search for courses</label>
      <div class="input-group">
        <input type="search" class="form-control input-lg disabled" autocomplete="off" autocapitalize="off"
               id="keywords"
               name="keywords" placeholder="Search for courses" disabled>
        <div class="input-group-btn">
          <button type="submit" class="btn btn-lg btn-primary disabled" disabled>
            Search
          </button>
        </div>
      </div>
    </div>
  </form>

  <p>Or, browse by department:</p>

  <#list faculties as faculty>
    <details>
      <summary>
        ${faculty.name}
      </summary>
      <ul>
        <#list departments as department>
          <#if department.faculty == faculty>
            <li>
              <a href="/modules/?department=${department.code}">${department.name}</a>
            </li>
          </#if>
        </#list>
      </ul>
    </details>
  </#list>
</@layout>
