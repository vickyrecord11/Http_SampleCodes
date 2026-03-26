import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class SimpleHttpServer {

    public static void main(String[] args) throws IOException {

        // Step 1: Create server at port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Step 2: Create endpoint (/hello)
        server.createContext("/hello", new MyHandler());

        // Step 3: Start server
        server.setExecutor(null); // default thread pool 
        server.start();

        System.out.println("Server started at http://localhost:8080/hello");
    }
}

// Step 4: Handler class
class MyHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // Step 5: Get request method (GET, POST, etc.)
        String requestMethod = exchange.getRequestMethod();
        System.out.println("Request Method: " + requestMethod);

        // Step 6: Prepare response
        String response = "Hello world!";

        // Step 7: Send response headers
        exchange.sendResponseHeaders(200, response.length());

        // Step 8: Write response body
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}