package handlers;

import java.io.*;
import java.net.*;

import com.sun.net.httpserver.*;

import com.google.gson.*;

import services.*;
import results.*;

public class FillHandler extends Handler {
    @Override
    protected boolean performHandle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().toLowerCase().equals("post")) {
            Gson gson = new Gson();

            int generations;
            String params = exchange.getRequestURI().toString();
            String[] paramsArray = params.split("/");
            String username = paramsArray[2];
            if (paramsArray.length == 3) {
                generations = 4;
            } else {
                generations = Integer.parseInt(paramsArray[3]);
            }

            FillService service = new FillService();
            FillResult result = service.performService(username,generations);

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