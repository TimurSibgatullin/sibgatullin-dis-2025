<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Тест Freemarker</title>
    <meta name="description" content="Тест Freemarker">
</head>
<body>
<h1>Регистрация</h1>
<div style="color:red">${errormessage!}</div>
<form action="/auth/userregister" method="post">
    <div>
        <label>Введите логин</label>
        <input type="text" name="username">
    </div>
    <div>
        <label>Введите пароль</label>
        <input type="password" name="password1">
    </div>
    <div>
        <label>Повторите пароль</label>
        <input type="password" name="password2">
    </div>
    <div></div>
    <div>
        <label>Введите роль</label>
        <input type="text" name="role">
    </div>
    <div>
        <label>Введите Имя</label>
        <input type="text" name="firstname">
    </div>
    <div>
        <label>Введите фамилию</label>
        <input type="text" name="lastname">
    </div>
    <div>
        <label>Введите телефон</label>
        <input type="text" name="phone">
    </div>
    <input type="submit" value="Зарегистрироваться">
</form>
</body>
</html>