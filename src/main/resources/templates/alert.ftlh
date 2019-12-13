<#include '*/layout.ftlh'>

<@layout title=title>
  <div class="alert alert-${class}">
    <i class="fad fa-${icon} fa-lg fa-fw"></i> ${message}
  </div>

  <#if continue??>
    <p>
      <a class="btn btn-default" href="${continue}">Continue</a>
    </p>
  </#if>
</@layout>
