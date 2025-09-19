package httpserver;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public interface IResourceService {
    void service(String method, Map<String, String> params, OutputStream os) throws IOException;
}
