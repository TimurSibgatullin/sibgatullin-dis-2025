<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Тест Freemarker</title>
    <meta name="description" content="Тест Freemarker">
</head>
<body>
<h1>Измените данные</h1>
<form action="${current_context}/showone" method="post">
    <input type="hidden" name="id" value="${id}" >
    <div>
        <label>Введите логин</label>
        <input type="text" name="login" value='${login}'>
    </div>
    <div>
        <label>Введите роль</label>
        <input type="text" name="role" value='${role}'>
    </div>
    <div>
        <label>Введите имя</label>
        <input type="text" name="firstname" value='${firstname}'>
    </div>
    <div>
        <label>Введите фамилию</label>
        <input type="text" name="lastname" value='${lastname}'>
    </div>
    <div>
        <label>Введите телефон</label>
        <input type="text" name="phone" value='${phone}'>
    </div>
    <input type="submit" value="Готово">
</form>
</body>
</html>