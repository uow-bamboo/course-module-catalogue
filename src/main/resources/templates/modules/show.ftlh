<#include '*/layout.ftlh'>

<@layout title="${module.code} ${module.title}">
  <h1>${module.code} ${module.title}</h1>

  <div class="pull-sm-right">
    <p>
      <a href="${module.code}.pdf" class="btn btn-default">
        <i class="fal fa-fw fa-file-pdf"></i>
        Download as PDF
      </a>
    </p>
  </div>

  <#include 'sections/summary.ftlh'>

  <div class="clearfix"></div>

  <ul class="nav nav-tabs" role="tablist">
    <li role="presentation" class="active">
      <a href="#description" role="tab" data-toggle="tab" aria-controls="description">Description</a>
    </li>
    <li role="presentation">
      <a href="#study" role="tab" data-toggle="tab" aria-controls="study">Study</a>
    </li>
    <li role="presentation">
      <a href="#assessment" role="tab" data-toggle="tab" aria-controls="assessment">Assessment</a>
    </li>
    <li role="presentation">
      <a href="#availability" role="tab" data-toggle="tab" aria-controls="availability">Availability</a>
    </li>
  </ul>

  <div class="tab-content">
    <div id="description" role="tabpanel" class="tab-pane active">
      <#include 'sections/description.ftlh'>
    </div>

    <div id="study" role="tabpanel" class="tab-pane">
      <#include 'sections/study.ftlh'>
    </div>

    <div id="assessment" role="tabpanel" class="tab-pane">
      <#include 'sections/assessment.ftlh'>
    </div>

    <div id="availability" role="tabpanel" class="tab-pane">
      <#include 'sections/availability.ftlh'>
    </div>
  </div>
</@layout>
