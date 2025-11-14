<#import "layout/base.ftlh" as base>
<@base.layout title="Книги">
    <h2>Книги жанра: ${genre.name}</h2>
    <#list books as book>
        <div class="book-card">
            <li><a href="/book?id=${book.id}">${book.title}</a> — <a href="/author?id=${book.author.id}">${book.author.nickname}</a></li>
        </div>
    </#list>
</@base.layout>
