<#import "layout/base.ftlh" as base>
<@base.layout title="${book.title}">
    <h2>${book.title}</h2>
    <p><b>Автор:</b> <a href="${currentContext}/user?id=${book.author.id}">${book.author.nickname}</a></p>
    <p><b>Жанр:</b> ${book.genre.name}</p>
    <p><b>Описание:</b> ${book.description}</p>
    <#if user??>
        <p><button id="favBtn"
                data-id="${book.id}"
                data-fav="${book.favorite?string("true", "false")}">
            ${book.favorite?string("Удалить из избранного", "Добавить в избранное")}
        </button></p>
        <script>
            document.getElementById("favBtn").addEventListener("click", function () {
                const btn = this;
                const bookId = btn.dataset.id;
                const isFav = btn.dataset.fav === "true";

                const url = isFav ? "${currentContext}/favorite/delete" : "${currentContext}/favorite/add";

                fetch(url, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded"
                    },
                    body: "id=" + encodeURIComponent(bookId)
                })
                    .then(r => r.text())
                    .then(resp => {
                        if (resp === "OK") {
                            if (isFav) {
                                btn.dataset.fav = "false";
                                btn.innerText = "Добавить в избранное";
                            } else {
                                btn.dataset.fav = "true";
                                btn.innerText = "Удалить из избранного";
                            }
                        } else {
                            alert("Ошибка: " + resp);
                        }
                    })
                    .catch(() => alert("Ошибка AJAX запроса"));
            });
        </script>
    </#if>

    <a href="${currentContext}/download?id=${book.id}">Скачать книгу</a>

    <h3>Комментарии</h3>
    <ul>
        <#list comments as c>
            <li><b>${c.created_at} — <a href="${currentContext}/user?id=${c.user.id}">${c.user.nickname}</a>:</b> ${c.text}</li>
        </#list>
    </ul>
    <form method="post" action="${currentContext}/book">
        <input type="hidden" name="bookId" value="${book.id}">
        <textarea name="comment" rows="3" cols="40" required></textarea><br>
        <button type="submit">Оставить комментарий</button>
    </form>
</@base.layout>
