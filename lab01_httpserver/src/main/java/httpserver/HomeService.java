package httpserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HomeService implements IResourceService {
    @Override
    public void service(String method, Map<String, String> params, OutputStream os) throws IOException {
        os.write("HTTP/1.1 200 OK\r\n".getBytes(StandardCharsets.UTF_8));
        os.write("Content-Type: text/html;chatset=UTF-8\r\n".getBytes(StandardCharsets.UTF_8));
        os.write("\r\n".getBytes(StandardCharsets.UTF_8));
        os.write("<html><body>Hello!!!</body></html>".getBytes(StandardCharsets.UTF_8));
    }
}
