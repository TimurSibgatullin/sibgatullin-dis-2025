<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Тест Аутентификации</title>
    <meta name="description" content="Тест Freemarker">
<style>
    label {
        color: dodgerblue;
        margin: 3px;
    }
    .div_style1 {
        border: 2px solid green;
    }
</style>
</head>
<body>
<a href="${current_context}/show">Показать все</a>
<a href="${current_context}/add">Добавить пользователя</a>

<form action="${current_context}/showone" method="get">
    <lable>Введите id пользователя</lable>
    <input type="text" name="id">
    <input type="submit" value="Найти">
</form>
</body>
</html>
