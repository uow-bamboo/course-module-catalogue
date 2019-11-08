<#include '*/layout.ftlh'>

<@layout title="Error">
  <h1>Error</h1>

  <p>
    Sorry, there's been a problem and we weren't able to complete your request.
  </p>

  <#if error??>
    <p>${error}</p>
  </#if>

  <#if trace??>
    <p>
      <span class="label label-danger">
        <i class="fad fa-user-secret"></i> Sysadmin only
      </span>
    </p>
    <pre>${trace}</pre>
  </#if>
</@layout>
