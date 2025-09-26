package ru.itis.dis403;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class TemplateHandler {
    public void handle(String templateName, Map<String, String> map, Writer writer) {
        try {
            //1. Найти файл по имени templateName
            String path = "templates/" + templateName;
            //2. Прочитать файл в строку
            String content = Files.readString(Path.of(path));

            for (Map.Entry<String, String> entry : map.entrySet()) {
                //3. Найти в файле ${param_name} и заменить на значения параметра
                String placeholder = "${" + entry.getKey() + "}";
                content = content.replace(placeholder, entry.getKey());
            }
            //4. Передать строку во writer
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException("ошибка при обработке шаблона " + templateName, e);
        }

    }
}
