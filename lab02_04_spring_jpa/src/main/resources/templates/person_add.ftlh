<html>
<head>
    <meta charset="UTF-8">
    <title>Добавить человека</title>
</head>
<body>

<h2>Добавить человека</h2>

<form action="/persons/add" method="post">

    <div>
        <label>Имя</label>
        <input type="text" name="name">
    </div>

    <div>
        <label>Телефон</label>
        <input type="text" name="phone">
    </div>

    <div>
        <label>Роль</label>
        <select name="role">
            <option value="ADMIN">Admin</option>
            <option value="CLIENT">Client</option>
        </select>
    </div>

    <button type="submit">Сохранить</button>

</form>

<a href="/persons">Посмотреть всех</a>

</body>
</html>