<header>
    <h1>FreeLib</h1>

    <nav>
        <a href="${currentContext}/home">Главная</a>
        <#if user??>
            |
            <a href="${currentContext}/profile">Профиль</a>

            <#if user.role == "author">
                |
                <a href="${currentContext}/upload">Загрузить книгу</a>
            </#if>

            <#if user.role == "admin">
                |
                <a href="${currentContext}/upload">Загрузить книгу</a>
                |
                <a href="${currentContext}/admin">Админ-панель</a>
            </#if>
            |
            <form action="${currentContext}/auth/logout" method="post" style="display:inline;">
                <button type="submit" class="link-button">Выход</button>
            </form>
        <#else>
            |
            <a href="${currentContext}/auth/usercheck">Вход</a>
            |
            <a href="${currentContext}/auth/userregister">Регистрация</a>
        </#if>
    </nav>

    <hr>
</header>