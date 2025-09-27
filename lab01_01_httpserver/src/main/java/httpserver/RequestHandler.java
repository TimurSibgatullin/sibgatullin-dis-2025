package httpserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class RequestHandler {
    final static Logger logger = LogManager.getLogger(RequestHandler.class);

    public void handle(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream os = clientSocket.getOutputStream()) {

            String lineOne = reader.readLine();
            logger.debug(lineOne);

            if (lineOne == null || lineOne.isEmpty()) {
                return;
            }

            String[] components = lineOne.split(" ");
            String method = components[0];
            String uri = components[1];

            //TODO реализовать определение метода (GET, POST,...) для передачи как параметра в сервис
            // http://localhost:8080/resource/part?name=tat&region=16
            // URI /resource/part
            //
            // При наличии извлечь параметры и поместить в Map

            String resource = uri;
            Map<String, String> params = new HashMap<>();

            int questionIndex = uri.indexOf("?");
            if (questionIndex != -1) {
                resource = uri.substring(0, questionIndex);
                String query = uri.substring(questionIndex + 1);
                for (String pair : query.split("&")) {
                    String[] keyValue = pair.split("=");
                    if (keyValue.length == 2) {
                        params.put(keyValue[0], keyValue[1]);
                    }
                }
            }

            String message;
            while ((message = reader.readLine()) != null && !message.isEmpty()) {
                logger.debug(message);
            }

            IResourceService resourceService = Application.resourceMap.get(resource);
            if (resourceService != null) {
                resourceService.service(method, params, os);
            } else {
                new NotFoundService().service(method, params, os);
            }

            os.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}