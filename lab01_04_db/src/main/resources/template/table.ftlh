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

<h1>Онлайн-табло
    <#if type?? && type == "arrival"> Прилёт
    <#else>Вылет</#if>
</h1>

<form method="get" action="/lab04/table">
    <label>Аэропорт:
        <select name="airport">
            <option value="SVO">Шереметьево</option>
            <option value="LED">Пулково</option>
            <option value="DME">Домодедово</option>
        </select>
    </label>

    <label>Дата:
        <input type="date" name="date" value="${date!}">
    </label>

    <label>Тип:
        <select name="type">
            <option value="departure">Вылет</option>
            <option value="arrival">Прилёт</option>
        </select>
    </label>

    <button type="submit">Показать</button>
</form>

<div class="tablo">
    <div class="tablo-row tablo-head">
        <div>Рейс</div>
        <div>Отправление</div>
        <div>Прибытие</div>
        <div>Статус</div>
        <div>Город</div>
        <div>Аэропорт</div>
    </div>
    <#if flights?has_content>
        <#list flights as f>
            <div class="tablo-row">
                <div>${f.flightId}</div>
                <div>${f.departure}</div>
                <div>${f.arrival}</div>
                <div>${f.status}</div>
                <div>${f.city}</div>
                <div>${f.airport}</div>
            </div>
        </#list>
    <#else>
        <div>Рейсов нет</div>
    </#if>
</div>

</body>
</html>