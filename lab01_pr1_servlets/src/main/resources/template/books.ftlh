<#import "layout/base.ftlh" as base>
<@base.layout title="Книги жанра ${genre.name}">
    <div class="container">
        <h2>Книги жанра: ${genre.name}</h2>
        <ul>
            <#if books?size == 0>
                <p>Книг этого жанра пока нет.</p>
            <#else>
                <#list books as book>
                    <div class="book-card">
                        <li><a href="${currentContext}/book?id=${book.id}">${book.title}</a> — <a href="/author?id=${book.author.id}">${book.author.nickname}</a></li>
                    </div>
                </#list>
            </#if>
        </ul>
    </div>
</@base.layout>
