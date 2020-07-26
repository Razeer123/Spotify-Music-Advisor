package com.derejmichal;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class AuthorizationApp {

    private boolean authorized = false;
    private StringBuilder query = new StringBuilder();
    private StringBuilder authCode = new StringBuilder();
    private StringBuilder spotifyServer = new StringBuilder();

    // Enter your ClientID and ClientSecret information here

    private String ClientID = "";
    private String ClientSecret = "";

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    public boolean isAuthorized() {

        if (!authorized) {

            System.out.println("Please, provide access for application.");

        }

        return authorized;

    }

    public void setServer(StringBuilder server) {
        this.spotifyServer.append(server);
    }

    public void startServer() throws IOException, InterruptedException {

        // Creating new server

        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);
        server.start();

        System.out.println("waiting for code...");

        // Creating context for the server

        server.createContext("/",
                httpExchange -> {

                    query.setLength(0);
                    query.append(httpExchange.getRequestURI().getQuery());
                    String message;

                    if (query.toString().contains("code")) {
                        message = "Got the code. Return back to your program.";
                    } else {
                        message = "Not found authorization code. Try again.";
                    }

                    httpExchange.sendResponseHeaders(200, message.length());
                    httpExchange.getResponseBody().write(message.getBytes());
                    httpExchange.getResponseBody().close();

                    authCode.append(query.substring(5));

                });

        // Waiting for the code

        while (authCode.toString().isEmpty()) {
            Thread.sleep(10);
        }

        // Stopping the server

        System.out.println("code received");
        server.stop(1);

        // Making requests

        System.out.println("making http request for access_token...");
        System.out.println("response:");

        // It will be deployed at a later stage. Change the port if 8080 is occupied.

        String linkData = "client_id=" + ClientID +
                "&client_secret=" + ClientSecret +
                "&grant_type=authorization_code" +
                "&code=" + authCode.toString() +
                "&redirect_uri=http://localhost:8080";

        // Creating POST request for Spotify servers

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(spotifyServer.toString() + "/api/token"))
                .timeout(Duration.ofMinutes(2))
                .POST(HttpRequest.BodyPublishers.ofString(linkData))
                .build();

        // Getting response

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

    }

}