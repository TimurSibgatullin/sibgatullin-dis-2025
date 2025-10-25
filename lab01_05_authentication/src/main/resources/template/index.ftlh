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
<h1>Поздравляю ${user!}, вы свой!</h1>

<form action="/auth/logout" method="get">
    <input type="submit" value="Выйти из учётки">
</form>
</body>
</html>