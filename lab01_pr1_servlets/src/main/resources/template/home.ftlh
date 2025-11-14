<#import "layout/base.ftlh" as base>
<@base.layout title="Главная страница">
<!DOCTYPE html>
<html lang="ru">
<#if user??>
<h2>Добро пожаловать, ${nickname!}</h2>
</#if>
<p><h3>Выберите жанр, чтобы начать читать:</h3></p>
<h2>Жанры</h2>
<ul>
    <#list genres as genre>
        <p>
            <li><a href="/books?genreId=${genre.id}">${genre.name}</a></li>
            <lable>Описание: ${genre.description}</lable>
        </p>
    </#list>
</ul>

<h2>Последние книги</h2>
<ul>
    <#list books as book>
        <li><a href="/book?id=${book.id}">${book.title}</a> — <a href="/user?id=${book.author.id}">${book.author.nickname}</a></li>
    </#list>
</ul>
</html>
</@base.layout>