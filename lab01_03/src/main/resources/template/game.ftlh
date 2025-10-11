<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Крестики-нолики</title>
    <style>
        table, td {
            border: 1px solid black;
            border-collapse: collapse;
        }
        td {
            width: 70px;
            height: 70px;
            text-align: center;
            vertical-align: middle;
        }
        input[type="image"] {
            border: none;
            background: none;
            width: 64px;
            height: 64px;
            cursor: pointer;
        }
        h2 {
            color: darkblue;
        }
        form {
            margin: 0;
            display: inline;
        }
    </style>
</head>
<body>
<h1>Крестики-нолики</h1>

<#if gameOver?? && gameOver>
    <h2>${message}</h2>
    <form method="get" action="/lab03/game">
        <button type="submit">Сыграть снова</button>
    </form>
<#else>
    <table>
        <#list table as r>
            <tr>
                <#assign i = r?index>
                <#list ["f","s","t"] as c>
                    <#assign j = c?index>
                    <td>
                        <#if r[c] == "пусто.png">
                            <form method="post" action="/lab03/game">
                                <input type="hidden" name="uid" value="${uid}">
                                <input type="hidden" name="row" value="${i}">
                                <input type="hidden" name="column" value="${j}">
                                <input type="image" src="/lab03/static/img/пусто.png" alt="."/>
                            </form>
                        <#else>
                            <img src="/lab03/static/img/${r[c]}" width="64" height="64">
                        </#if>
                    </td>
                </#list>
            </tr>
        </#list>
    </table>
</#if>

</body>
</html>