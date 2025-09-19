package httpserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class NotFoundService implements IResourceService {
    @Override
    public void service(String method, Map<String, String> params, OutputStream os) throws IOException {
        os.write("HTTP/1.1 404 Not Found\r\n".getBytes(StandardCharsets.UTF_8));
        os.write("Content-Type: text/html;charset=UTF-8\r\n".getBytes(StandardCharsets.UTF_8));
        os.write("\r\n".getBytes(StandardCharsets.UTF_8));
        os.write("<html><body>404 Page Not Found!</body></html>:".getBytes(StandardCharsets.UTF_8));
    }
}
