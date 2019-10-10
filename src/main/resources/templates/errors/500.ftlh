<#include '*/layout.ftlh'>

<@layout title="Error">
  <h1>Error</h1>

  <p>
    Sorry, there's been a problem and we weren't able to complete your request.
  </p>

  <#if message??>
    <p>${message}</p>
  </#if>

  <#if stackTrace??>
    <p>
      <span class="label label-danger">
        <i class="fad fa-user-secret"></i> Sysadmin only
      </span>
    </p>
    <pre>${stackTrace}</pre>
  </#if>
</@layout>
