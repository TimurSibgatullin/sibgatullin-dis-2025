<!DOCTYPE html>
<#import "layout/base.ftlh" as base>
<@base.layout title="Вход">
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Freelib вход</title>
    <meta name="description" content="Freelib вход">
</head>
<body>
<h2>Введите логин/пароль</h2>
<div style="color:red">${errormessage!}</div>
<form action="/auth/usercheck" method="post">
    <div>
        <label>Логин</label>
        <input type="text" name="username">
    </div>
    <div>
        <label>Пароль</label>
        <input type="password" name="password">
    </div>
    <input type="submit" value="Вход">
</form>
<form action="/auth/userregister" method="get">
    <div>
        <label>Ещё не зарегистрированы?</label>
        <input type="submit" value="Зарегистрироваться">
    </div>
</form>
</body>
</html>
</@base.layout>