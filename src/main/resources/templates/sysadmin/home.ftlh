<#include '*/layout.ftlh'>

<@layout title="System admin">
  <h1>System admin</h1>

  <form action="/sysadmin/index/modules" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

    <div class="form-group">
      <button class="btn btn-default">Index modules now</button>
    </div>
  </form>
</@layout>
