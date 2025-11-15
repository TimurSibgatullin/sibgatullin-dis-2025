<#ftl encoding="UTF-8">
<#import "../layout/base.ftlh" as base>
<@base.layout title="Профиль">
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Freelib регистрация</title>
    <meta name="description" content="Freelib регистрация">
</head>
<body>
<h2>Регистрация</h2>
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
    <div>
        <label>Выберите роль</label>
        <select id="role" name="role">
            <option value="reader" selected>Читатель</option>
            <option value="author">Автор</option>
        </select>
    </div>
    <div>
        <label>Введите никнейм</label>
        <input type="text" name="nickname">
    </div>
    <input type="submit" value="Зарегистрироваться">
</form>
</body>
</html>
</@base.layout>