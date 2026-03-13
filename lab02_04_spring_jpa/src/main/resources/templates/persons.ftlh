<html>
<head>
    <meta charset="UTF-8">
    <title>Список людей</title>
</head>
<body>

<h2>Список людей</h2>

<a href="/persons/add">Добавить человека</a>

<div>

    <#list persons as person>
        <div>
            <span>${person.id}</span>
            <span>${person.name}</span>
        </div>
    </#list>

</div>

</body>
</html>