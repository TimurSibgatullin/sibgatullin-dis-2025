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
        try {
            // Поток для чтения данных от клиента
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            // Читаем пакет от клиента
            String lineOne = reader.readLine();
            System.out.println(lineOne);
            logger.debug(lineOne);
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

            if (resource.equals("/shutdown")) {
                logger.info("server stopped by client");
                //break;
            }
            while (true) {
                // Читаем пакет от клиента
                String message = reader.readLine();
                System.out.println(message);
                logger.debug(message);

                if (message.isEmpty()) {
                    logger.debug("end of request header");
                    OutputStream os = clientSocket.getOutputStream();
                    logger.debug("outputStream" + os);
                    IResourceService resourceService = Application.resourceMap.get(resource);
                    if (resourceService != null) {
                        resourceService.service(method, params, os);
                    } else {
                        new NotFoundService().service(method, params, os);
                    }
                    os.flush();
                    logger.debug("outputStream" + os);
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
