package ru.itis.dis403;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class TemplateHandler {
    public void handle(String templateName, Map<String, String> map, Writer writer) {
        try (InputStream is = getClass().getResourceAsStream("/templates/" + templateName)) {
            if (is == null) {
                throw new RuntimeException("Шаблон не найден: " + templateName);
            }

            String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            for (Map.Entry<String, String> entry : map.entrySet()) {
                String placeholder = "${" + entry.getKey() + "}";
                content = content.replace(placeholder, entry.getValue());
            }

            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при обработке шаблона " + templateName, e);
        }

    }
}
