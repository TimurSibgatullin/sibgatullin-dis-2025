<#import "layout/base.ftlh" as base>
<@base.layout title="${book.title}">
    <h2>${book.title}</h2>
    <p><b>Автор:</b> <a href="/user?id=${book.author.id}">${book.author.nickname}</a></p>
    <p><b>Жанр:</b> ${book.genre.name}</p>
    <p><b>Описание:</b> ${book.description}</p>
    <#if user??>
        <p><form action="${currentContext}/favorite/add" method="post">
            <input type="hidden" name="bookId" value="${book.id}">
            <button type="submit">Добавить в избранное</button>
        </form></p>
        <p><form action="${currentContext}/favorite/delete" method="post">
            <input type="hidden" name="bookId" value="${book.id}">
            <button type="submit">Удалить из избранного</button>
        </form></p>
    </#if>

    <a href="/download?id=${book.id}">Скачать книгу</a>

    <h3>Комментарии</h3>
    <ul>
        <#list comments as c>
            <li><b>${c.created_at} — <a href="/user?id=${c.user.id}">${c.user.nickname}</a>:</b> ${c.text}</li>
        </#list>
    </ul>
    <form method="post" action="/book">
        <input type="hidden" name="bookId" value="${book.id}">
        <textarea name="comment" rows="3" cols="40" required></textarea><br>
        <button type="submit">Оставить комментарий</button>
    </form>
</@base.layout>