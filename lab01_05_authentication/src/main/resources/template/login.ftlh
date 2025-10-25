<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Тест Freemarker</title>
    <meta name="description" content="Тест Freemarker">
</head>
<body>
<h1>Введите логин/пароль</h1>
<div style="color:red">${errormessage!}</div>
<form action="/auth/usercheck" method="post">
    <div>
        <label>Имя пользака</label>
        <input type="text" name="username">
    </div>
    <div>
        <label>Пароль</label>
        <input type="password" name="password">
    </div>
    <input type="submit" value="Вход">
</form>
<form action="/auth/register" method="get">
    <div>
        <label>Ещё не зарегистрированы?</label>
        <input type="submit" value="Зарегистрироваться">
    </div>
</form>
</body>
</html>