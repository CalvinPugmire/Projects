package handlers;

import java.io.*;
import java.net.*;

import com.sun.net.httpserver.*;

import com.google.gson.*;

import services.*;
import requests.*;
import results.*;

public class LoginHandler extends Handler {

    @Override
    public boolean performHandle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().toLowerCase().equals("post")) {
            Gson gson = new Gson();

            InputStream reqBody = exchange.getRequestBody();
            String reqData = readString(reqBody);

            LoginRequest request = gson.fromJson(reqData, LoginRequest.class);
            LoginService service = new LoginService();
            LoginResult result = service.performService(request);

            OutputStream resBody = exchange.getResponseBody();
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
    }
}