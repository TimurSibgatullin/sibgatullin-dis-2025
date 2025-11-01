<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Все пользователи</title>
    <meta name="description" content="Самолеты">
    <style>
        .table {
            display: flex;
            flex-direction: column;
        }
        .table-row {
            display: flex;
            flex-direction: row;
            font-weight: lighter;
        }
        .table-head {
            font-weight: bold;
        }
        .table-row > div {
            padding: 8px 12px;
            flex: 1;
        }
    </style>
</head>
<h1>Пользователи</h1>
<div class="table">
    <div class="table-row table-head">
        <div>id</div>
        <div>Логин</div>
        <div>Роль</div>
        <div>Имя</div>
        <div>Фамилия</div>
        <div>Телефон</div>
    </div>
    <#list users as u>
        <div class="table-row">
            <div>${u.id}</div>
            <div>${u.login}</div>
            <div>${u.role}</div>
            <div>${u.firstname}</div>
            <div>${u.lastname}</div>
            <div>${u.phone}</div>
        </div>
    </#list>
</div>
<form action="${current_context}/" method="get">
    <input type="submit" value="Назад">
</form>
