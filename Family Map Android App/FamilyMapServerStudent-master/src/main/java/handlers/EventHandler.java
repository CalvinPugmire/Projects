package handlers;

import java.io.*;
import java.net.*;

import com.sun.net.httpserver.*;

import com.google.gson.*;

import services.*;
import results.*;

public class EventHandler extends Handler {
    @Override
    protected boolean performHandle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().toLowerCase().equals("get")) {
            // Get the HTTP request headers
            Headers reqHeaders = exchange.getRequestHeaders();

            if (reqHeaders.containsKey("Authorization")) {
                // Extract the auth token from the "Authorization" header
                String authtoken = reqHeaders.getFirst("Authorization");

                EventService service = new EventService();
                EventResult result = service.performService(authtoken);

                OutputStream resBody = exchange.getResponseBody();
                Gson gson = new Gson();
                String resData = gson.toJson(result);

                // Start sending the HTTP response to the client, starting with
                // the status code and any defined headers.
                if (result.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }

                writeString(resData, resBody);

                resBody.close();

                // We are not sending a response body, so close the response body
                // output stream, indicating that the response is complete.
                exchange.getResponseBody().close();

                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}