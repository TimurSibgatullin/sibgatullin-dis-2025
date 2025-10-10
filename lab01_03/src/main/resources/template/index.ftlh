<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Тест Freemarker</title>
    <meta name="description" content="Тест Freemarker">
<style>
    label {
        color: dodgerblue;
        margin: 3px;
    }
    .div_style1 {
        border: 2px solid green;
    }
    .div_style2 {
        border: 2px solid yellow;
    }
    .div_style3 {
        border: 2px solid red;
    }
    .div_style4 {
        border: 2px solid darkcyan;
        color: darkcyan;
    }
    div {
        margin: 3px;
    }
</style>
</head>
<body>
<h1>Тест Freemarker</h1>
<img src="/lab01_03/static/img/images.jpg"/>
<form method='post' action='test'>
    <div>
        <label>Surname:</label>
        <input type='text' name='surname' value='${surname}'></input>
    </div>
    <div>
        <label>Name:</label>
        <input type='text' name='name' value='${name}'></input>
    </div>
    <div>
        <label>Role:</label>
        <select name='role'>
            <option value="1">Студент</option>
            <option value="2">Преподаватель</option>
            <option value="3">Вольнопосещающий</option>
        </select>
    </div>
    <div>
        <label>Accept all cookies</label>
        <input type="checkbox" name="accept">
    </div>
    <fieldset>
        <div class="div_style1">
            <label>Norm niche tak</label>
            <input type="radio" name="mood" value="good" checked>
        </div>
        <div class="div_style2">
            <label>Sososo</label>
            <input type="radio" name="mood" value="fifty-fifty" checked>
        </div>
        <div class="div_style3">
            <label>Sovsem beda prosto</label>
            <input type="radio" name="mood" value="bad" checked>
        </div>
    </fieldset>
    <div class="div_style4">
        <input type='submit' value='Send'></input>
    </div>
</form>
<table width="300">
    <#list table as r>
        <tr>
            <td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td>
        </tr>
        <tr>
            <td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td>
        </tr>
        <tr>
            <td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td>
        </tr>
    </#list>
</table>
</body>
</html>