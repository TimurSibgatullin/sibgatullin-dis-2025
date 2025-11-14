<#macro layout title>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
</head>
<body>

<#include "header.ftlh">

<main><#nested/></main>

<#include "footer.ftlh">

</body>
</html>
</#macro>