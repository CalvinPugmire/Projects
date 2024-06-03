package handlers;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;

import com.sun.net.httpserver.*;

public class FileHandler extends Handler {
    @Override
    public boolean performHandle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().toLowerCase().equals("get")) {
            String urlPath = exchange.getRequestURI().toString();

            if (urlPath == null || urlPath.equals("/")) {
                urlPath = "/index.html";
            }

            String filePath = "web" + urlPath;

            File file = new File(filePath);

            if (file.exists()) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                OutputStream respBody = exchange.getResponseBody();

                Files.copy(file.toPath(), respBody);

                respBody.close();

                return true;
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);

                OutputStream respBody = exchange.getResponseBody();

                Files.copy(Path.of("web/HTML/404.html"), respBody);

                respBody.close();

                return false;
            }
        } else {
            return false;
        }
    }
}