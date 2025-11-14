<#ftl encoding="UTF-8">
<#import "layout/base.ftlh" as base>
<@base.layout title="Профиль">
    <h2>Здравствуйте, ${user.nickname}!</h2>
    <h3>Книги в избранном:</h3>
    <#if myFavoriteBooks?size == 0>
        <p>Вы пока не добавили ни одной книги в избранное.</p>
    <#else>
        <ul>
            <#list myFavoriteBooks as book>
                <li><a href="/book?id=${book.id}">${book.title}</a></li>
            </#list>
        </ul>
    </#if>
    <#if user.role == "author" || user.role == "admin">
        <h3>Ваши книги:</h3>
        <#if myBooks?size == 0>
            <p>Вы пока не добавили ни одной книги.</p>
        <#else>
            <ul>
                <#list myBooks as book>
                    <li><a href="/book?id=${book.id}">${book.title}</a></li>
                </#list>
            </ul>
        </#if>
        <a href="/upload">Добавить новую книгу</a>
    </#if>

</@base.layout>
