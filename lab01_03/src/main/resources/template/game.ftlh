<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Крестики нолики</title>
    <meta name="description" content="Крестики нолики">
</head>
<body>
<h1>Тест Freemarker</h1>

<form method='post' action='/lab03/game'>
</form>
    <table width="300">
        <#list table as r>
            <tr>
                <td><img src="/lab01_03/static/img/images.jpg"/> </td><td>2</td><td>3</td>
            </tr>
            <tr>
                <td>1</td><td>2</td><td>3</td>
            </tr>
            <tr>
                <td>1</td><td>2</td><td>3</td>
            </tr>
        </#list>
    </table>
</body>
</html>