package Tools;

import Controles.Register;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class OAuth2CallbackHandler {
    private static final int PORT = 8080;
    private static final String CALLBACK_URL = "http://localhost:" + PORT;
    private HttpServer server;

    public void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/", new CallbackHandler());
        server.start();
        System.out.println("OAuth2 callback server started on port " + PORT);
    }

    public static void main(String[] args) throws IOException {
        // Create HTTP server
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // Create context for handling callback requests
        server.createContext("/", new OAuth2CallbackHandler.CallbackHandler());

        // Start the server
        server.start();

        System.out.println("Server started on port " + PORT);
    }

    private static class CallbackHandler implements HttpHandler {
        Register rg = new Register();
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "Callback received successfully!";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

            // Extract authorization code from request parameters
            String authorizationCode = exchange.getRequestURI().getQuery().split("=")[1];

            // Exchange authorization code for access token
            try {
                GoogleTokenResponse tokenResponse = rg.exchangeCodeForAccessToken(authorizationCode);
                // Process token response...
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
