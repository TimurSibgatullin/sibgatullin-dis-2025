<!DOCTYPE html>
<#import "../layout/base.ftlh" as base>
<@base.layout title="Создание автора">
<html lang="ru">
<body>
<h2>Внесите данные в поля</h2>
<div style="color:red">${errormessage!}</div>
<form action="/admin/create-author" method="post">
    <p>
        <label>Имя</label>
        <input type="text" name="nickname" value="" required>
    </p>
    <p>
        <label>Описание</label>
        <input type="text" name="description" value="" required>
    </p>
    <input type="submit" value="Cоздать">
</form>
</body>
</html>
</@base.layout>