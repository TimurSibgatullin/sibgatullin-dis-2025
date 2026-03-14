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
            ${person.id}
            ${person.name}
            ${(person.phone.number)!"нет телефона"}
            ${person.role}
        </div>
    </#list>

</div>

</body>
</html>