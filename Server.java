import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;

public class Server {

    static List<String> users = new ArrayList<>();

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8090), 0);

        server.createContext("/users", new UserHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("Server started at http://localhost:8090");
    }

    static class UserHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            String method = exchange.getRequestMethod();

            if (method.equals("GET")) {
                handleGet(exchange);
            } else if (method.equals("POST")) {
                handlePost(exchange);
            } else if (method.equals("PUT")) {
                handlePut(exchange);
            } else if (method.equals("DELETE")) {
                handleDelete(exchange);
            }
        }

        // 🔹 GET
        private void handleGet(HttpExchange exchange) throws IOException {
            String response = users.toString();
            sendResponse(exchange, response);
        }

        // 🔹 POST (Create)
        private void handlePost(HttpExchange exchange) throws IOException {

            InputStream input = exchange.getRequestBody();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            StringBuilder body = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                body.append(line);
            }

            String newUser = body.toString();
            users.add(newUser);

            sendResponse(exchange, "User added: " + newUser);
        }

        // 🔹 PUT (Update)
        private void handlePut(HttpExchange exchange) throws IOException {
            sendResponse(exchange, "Update not implemented yet");
        }

        // 🔹 DELETE
        private void handleDelete(HttpExchange exchange) throws IOException {
            users.clear();
            sendResponse(exchange, "All users deleted");
        }

        // 🔹 COMMON RESPONSE METHOD
        private void sendResponse(HttpExchange exchange, String response) throws IOException {

            exchange.getResponseHeaders().set("Content-Type", "text/plain");

            exchange.sendResponseHeaders(200, response.getBytes().length);

            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
