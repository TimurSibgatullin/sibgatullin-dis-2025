<#import "layout/base.ftlh" as base>
<@base.layout title="Главная страница">
<h2>Добро пожаловать, ${nickname!}</h2>
<p>Выберите жанр, чтобы начать читать:</p>
<ul>
    <#list genres as genre>
        <p>
        <li><a href="/books?genreId=${genre.id}">${genre.name}</a></li>
        <lable>Описание: ${genre.description}</lable>
        </p>
    </#list>
</ul>
</@base.layout>