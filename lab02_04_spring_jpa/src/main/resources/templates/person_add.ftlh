<html>
<head>
    <meta charset="UTF-8">
    <title>Добавить человека</title>
</head>
<body>

<h2>Добавить человека</h2>

<form action="/persons/add" method="post">

    <div>
        <label>ID</label>
        <input type="text" name="id">
    </div>

    <div>
        <label>Имя</label>
        <input type="text" name="name">
    </div>

    <button type="submit">Сохранить</button>

</form>

<a href="/persons">Посмотреть всех</a>

</body>
</html>