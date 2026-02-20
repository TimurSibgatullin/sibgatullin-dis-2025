package org.example.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Method;

public class DispatcherServlet extends HttpServlet {

    private Context applicationContext;

    public DispatcherServlet(Context context) {
        this.applicationContext = context;
    }

    /**
     * Этот метод вызывается для всех HTTP-методов (GET, POST и т.д.).
     * Мы будем обрабатывать только GET, как указано в задании.
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Получаем путь из URI запроса
        String path = req.getRequestURI();

        // Томкат добавляет контекстный путь (обычно корень ""), но если вы его измените,
        // может потребоваться его удалить из пути. Пока просто возьмем URI как есть.
        // Также GET-параметры (?param=value) нам не нужны для сопоставления путей.
        // req.getRequestURI() возвращает путь без параметров, так что это приемлемо для базовой реализации.
        // Пример: http://localhost:8090/home -> path = "/home"
        // Пример: http://localhost:8090/ -> path = "/"
        // Пример: http://localhost:8090/api/users?id=1 -> path = "/api/users"

        System.out.println("Получен запрос: " + req.getMethod() + " " + path);

        // Находим зарегистрированный обработчик для этого пути
        HandlerMethod handler = applicationContext.getHandlerForPath(path);

        if (handler != null) {
            // Нашли обработчик!
            Object controllerInstance = handler.getControllerInstance();
            Method method = handler.getMethod();

            try {
                // Проверяем, совпадает ли HTTP-метод запроса с ожидаемым.
                // У нас только @GetMapping, поэтому ожидаем GET.
                // В реальной системе тут будет проверка на основе аннотации (@PostMapping, @PutMapping и т.д.)
                if (!"GET".equalsIgnoreCase(req.getMethod())) {
                    resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    resp.getWriter().println("Метод " + req.getMethod() + " не разрешен для " + path);
                    return;
                }

                // Вызываем метод контроллера.
                // В данном случае метод ожидает два аргумента: HttpServletRequest и HttpServletResponse
                // Мы передаем их в том же порядке.
                method.invoke(controllerInstance, req, resp);

            } catch (IllegalAccessException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().println("Ошибка доступа к методу обработчика: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) { // Обработка InvocationTargetException и других
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().println("Ошибка при выполнении метода обработчика: " + e.getCause().getMessage()); // getCause(), т.к. invoke оборачивает в InvocationTargetException
                e.printStackTrace();
            }
        } else {
            // Обработчик не найден
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().println("Ресурс не найден: " + path);
        }
    }
}
