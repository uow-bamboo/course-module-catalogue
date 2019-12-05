<#include '*/layout.ftlh'>

<@layout title="Home">
  <h1>Home</h1>

  <p>
    Welcome to the course and module catalogue at Warwick. Here you can browse all the courses and modules we’re
    currently running, search for courses and modules containing a particular word or phrase, or check the details of a
    course or module as it was in previous years.
  </p>

  <form method="get" action="/courses" name="courses" class="home-search">
    <div class="form-group">
      <label for="keywords" class="sr-only">Search for a course</label>
      <div class="input-group">
        <input type="search" class="form-control input-lg disabled" autocomplete="off" autocapitalize="off"
               id="keywords"
               name="keywords" placeholder="Search for a course" disabled>
        <div class="input-group-btn">
          <button type="submit" class="btn btn-lg btn-primary disabled" disabled>
            Search
          </button>
        </div>
      </div>
    </div>
  </form>

  <div class="form-group">
    <p>
      Courses are what you take for the whole of your degree, usually running over one or more years, made up of a set
      of
      modules.
    </p>
  </div>

  <form method="get" action="/modules" name="modules" class="home-search">
    <div class="form-group">
      <label for="keywords" class="sr-only">Search for a module</label>
      <div class="input-group">
        <input type="search" class="form-control input-lg" autocomplete="off" autocapitalize="off" id="keywords"
               name="keywords" placeholder="Search for a module">
        <div class="input-group-btn">
          <button type="submit" class="btn btn-lg btn-primary">
            Search
          </button>
        </div>
      </div>
    </div>
  </form>

  <div class="form-group">
    <p>
      Modules are the building blocks of a course, often running for about a single academic term.
    </p>
  </div>

  <div class="home-search">
    <p>Or, browse by department:</p>

    <#list faculties as faculty>
      <details>
        <summary>${faculty.name}</summary>
        <ul>
          <#list departments?filter(d -> d.faculty == faculty) as department>
            <li>
              <a href="/modules/?department=${department.code}">${department.name}</a>
            </li>
          </#list>
        </ul>
      </details>
    </#list>
  </div>
</@layout>
