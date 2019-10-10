<#include '*/layout.ftlh'>

<@layout title="Permission denied">
  <h1>Permission denied</h1>

  <p>
      <#if identity??>
        Sorry
          <#if actualIdentity?? && actualIdentity != identity>
              ${identity} (really ${actualIdentity}),
          <#else>
              ${identity},
          </#if>
      <#else>
        Sorry,
      </#if>
    you don't have permission to see that.
  </p>
</@layout>
