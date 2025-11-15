<#import "../layout/base.ftlh" as base>
<@base.layout title="Админская панель">
    <!DOCTYPE html>
    <html lang="ru">
    <h2>Добро пожаловать домой, ${nickname!}.</h2>
    <p>
        <form method="get" action="${currentContext}/admin/create-genre">
            <button type="submit">Создать жанр</button>
        </form>
    </p>
    <p>
    <form method="get" action="${currentContext}/admin/all-genres">
        <button type="submit">Показать все жанры</button>
    </form>
    </p>
    <p>
        <form method="get" action="${currentContext}/admin/create-author">
            <button type="submit">Создать автора</button>
        </form>
    </p>
    <p>
        <div style="color:red">${errormessage!}</div>
        <form method="post" action="${currentContext}/admin">
            <label>Войти в профиль автора:</label>
            <input type="text" name="author" value="" required>
            <button type="submit">Войти</button>
        </form>
    </p>
    <p>
        <form method="get" action="${currentContext}/admin/all-authors">
            <button type="submit">Показать всех авторов</button>
        </form>
    </p>
    </html>
</@base.layout>
