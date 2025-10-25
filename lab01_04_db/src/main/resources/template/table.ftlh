<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Онлайн-табло</title>
    <style>
        .tablo { display: flex; flex-direction: column; margin-top: 20px; }
        .tablo-row { display: flex; padding: 8px 12px; border-bottom: 1px solid #ccc; }
        .tablo-head { font-weight: bold; background-color: #ddd; }
        .tablo-row > div { flex: 1; }
        form { margin-bottom: 20px; }
        form > * { margin-right: 10px; }
    </style>
</head>
<body>
<h1>Онлайн-табло (${type!"departure"? "Прилёт" : "Вылет"})</h1>

<!-- Форма выбора -->
<form method="get" action="/tablo">
    <label>Аэропорт:
        <select name="airport">
            <#if airport == "SVO">значение1<#else>значение2</#if>
            <option value="SVO" ${airport == "SVO" ? "selected"}>Шереметьево</option>
            <option value="LED" ${airport == "LED" ? "selected"}>Пулково</option>
            <option value="DME" ${airport == "DME" ? "selected"}>Домодедово</option>
            <!-- Добавь свои аэропорты -->
        </select>
    </label>

    <label>Дата:
        <input type="date" name="date" value="${date!}">
    </label>

    <label>Тип:
        <select name="type">
            <option value="departure" ${type == "departure" ? "selected"}>Вылет</option>
            <option value="arrival" ${type == "arrival" ? "selected"}>Прилет</option>
        </select>
    </label>

    <button type="submit">Показать</button>
</form>

<!-- Таблица рейсов -->
<div class="tablo">
    <div class="tablo-row tablo-head">
        <div>Рейс</div>
        <div>Отправление</div>
        <div>Прибытие</div>
        <div>Статус</div>
        <div>Город</div>
        <div>Аэропорт</div>
    </div>

    <#list flights as f>
        <div class="tablo-row">
            <div>${f.flightId}</div>
            <div>${f.departure?string("HH:mm")}</div>
            <div>${f.arrival?string("HH:mm")}</div>
            <div>${f.status}</div>
            <div>${f.city}</div>
            <div>${f.airport}</div>
        </div>
    </#list>
</div>
</body>
</html>
