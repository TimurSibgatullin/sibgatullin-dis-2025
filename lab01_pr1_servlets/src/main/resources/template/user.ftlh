<#ftl encoding="UTF-8">
<#import "layout/base.ftlh" as base>
<@base.layout title="Пользователь ${user.nickname}">
    <h2>Профиль пользователя ${user.nickname}</h2>
    <#if user.role == "author" || user.role == "admin">
        <h3>Книги этого автора:</h3>
        <#if myBooks?size == 0>
            <p>Автор пока не добавил ни одной книги.</p>
        <#else>
            <ul>
                <#list myBooks as book>
                    <li><a href="/book?id=${book.id}">${book.title}</a></li>
                </#list>
            </ul>
        </#if>
    </#if>
    <h3>Комментарии пользователя: ${user.nickname}</h3>
    <#if myComments?size == 0>
        <p>Вы пока не оставляли комментариев.</p>
    <#else>
        <ul>
            <#list myComments as comment>
                <li>Комментарий к: <a href="/book?id=${comment.book.id}">${comment.book.title}</a> от ${comment.created_at} — ${comment.text}</li>
            </#list>
        </ul>
    </#if>
</@base.layout>
