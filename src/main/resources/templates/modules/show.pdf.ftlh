<!doctype html>
<html lang="en">
<head>
  <title></title>
  <style>
    body {
      font-family: Helvetica;
      font-size: 12pt;
    }

    h1, h2, h3, h4, h5, p, table {
      margin-top: 0;
      margin-bottom: 10pt;
    }

    h1, h2, h3, h4, h5 {
      margin-top: 20pt;
    }

    h1 {
      font-size: 28pt;
    }

    h2 {
      font-size: 16pt;
    }

    h5 {
      font-size: 13pt;
    }

    table {
      width: 100%;
    }

    th {
      text-align: left;
    }

    dt {
      font-weight: bold;
    }

    dd ul {
      margin-top: 0;
      padding-left: 0;
    }

    hr {
      margin-top: 20pt;
      margin-bottom: 20pt;
      border: 0;
      background-color: #666;
      height: 1pt;
    }
  </style>
</head>
<body>

<h1>${module.code} ${module.title}</h1>
<h2>${academicYear}</h2>

<#include 'sections/summary.ftlh'>

<hr>

<h2>Description</h2>
<#include 'sections/description.ftlh'>

<hr>

<h2>Study</h2>
<#include 'sections/study.ftlh'>

<hr>

<h2>Assessment</h2>
<#include 'sections/assessment.ftlh'>

<hr>

<h2>Availability</h2>
<#include 'sections/availability.ftlh'>

</body>
</html>

