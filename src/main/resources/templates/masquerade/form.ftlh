<#include '*/layout.ftlh'>

<@layout title="Masquerade">
  <h1>Masquerade</h1>

  <form method="post" class="form-inline">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

    <div class="input-group">
      <label for="username" class="sr-only">Type a usercode</label>
      <input type="text" name="username" class="form-control flexi-picker" data-include-groups="false"
             placeholder="Type a usercode" autofocus autocomplete="off">
      <div class="input-group-btn">
        <button type="submit" class="btn btn-default">
          <i class="fal fa-fw fa-user-secret"></i>
          Mask
        </button>
      </div>
    </div>
  </form>
</@layout>
