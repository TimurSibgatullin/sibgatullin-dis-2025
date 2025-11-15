<!DOCTYPE html>
<#import "../layout/base.ftlh" as base>
<@base.layout title="Cjplfybt жанра">
<html lang="ru">
<body>
<h2>Внесите данные в поля</h2>
<div style="color:red">${errormessage!}</div>
<form action="${currentContext}/admin/create-genre" method="post">
    <p>
        <label>Название</label>
        <input type="text" name="name" value="" required>
    </p>
    <p>
        <label>Описание</label>
        <textarea id="text" name="description" rows="5" cols="40" required></textarea>
    </p>
    <input type="submit" value="Создать">
</form>
</body>
</html>
</@base.layout>
