<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Тест Freemarker</title>
    <meta name="description" content="Тест Freemarker">
</head>
<body>
<h1>Введите что-то чтобы создать пользователя</h1>
<form action="${current_context}/add" method="post">
    <div>
        <label>Введите логин</label>
        <input type="text" name="login">
    </div>
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
    <input type="submit" value="Готово">
</form>
</body>
</html>