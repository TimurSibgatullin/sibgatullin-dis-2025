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
                <a href="${currentContext}/admin">Админ-панель</a>
            </#if>
            |
            <a href="${currentContext}/auth/logout">Выход</a>

        <#else>
            |
            <a href="${currentContext}/auth/usercheck">Вход</a>
            |
            <a href="${currentContext}/auth/userregister">Регистрация</a>
        </#if>
    </nav>

    <hr>
</header>