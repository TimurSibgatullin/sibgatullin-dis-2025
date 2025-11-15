<#macro layout title>
    <!DOCTYPE html>
    <html lang="ru">
    <head>
        <meta charset="UTF-8">
        <title>${title}</title>
        <link rel="stylesheet" href="${currentContext}/static/css/style.css">
    </head>
    <body>

    <div class="container">
        <#include "header.ftlh">

        <main><#nested/></main>

        <#include "footer.ftlh">
    </div>

    </body>
    </html>
</#macro>