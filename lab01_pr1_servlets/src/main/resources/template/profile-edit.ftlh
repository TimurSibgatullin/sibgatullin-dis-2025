<!DOCTYPE html>
<#import "layout/base.ftlh" as base>
<@base.layout title="Редактировать свой профиль">
<html lang="ru">
<body>
<h3>Вы можете изменить данные в полях:</h3>
<div style="color:red">${errormessage!}</div>
<form action="/profile-edit" method="post">
    <p>
        <label>Имя</label>
        <input type="text" name="nickname" value="${author.nickname}" required>
    </p>
    <p>
        <label>Обо мне</label>
        <input type="text" name="description" value="${author.description!}" required>
    </p>
    <input type="submit" value="Изменить">
</form>
<form action="/profile-edit" method="post">
    <p>
        <input type="hidden" name="delete" value="true">
        <input type="submit" value="Удалить">
    </p>
</form>
<#if user.role == "author">
    <h3>Ваши книги:</h3>
    <#if myBooks?size == 0>
        <p>Вы пока не добавили ни одной книги.</p>
    <#else>
        <#list myBooks as book>
            <p>
            <form method="get" action="${currentContext}/edit-my-book">
                <input type="hidden" name="id" value="${book.id}" required>
                <button type="submit">${book.title}</button>
            </form>
            </p>
        </#list>
    </#if>
</#if>
</body>
</html>
</@base.layout>