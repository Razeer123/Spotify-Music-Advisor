package com.derejmichal;

import com.google.gson.*;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class AuthorizationApp {

    private boolean authorized = false;
    private final StringBuilder query = new StringBuilder();
    private final StringBuilder authCode = new StringBuilder();
    private final StringBuilder spotifyServer = new StringBuilder();
    private final StringBuilder accessToken = new StringBuilder();
    private final List<String> name = new ArrayList<>();
    private final List<String> artist = new ArrayList<>();
    private final List<String> link = new ArrayList<>();

// Enter your ClientID and ClientSecret information here

    private String ClientID = "";
    private String ClientSecret = "";

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    public List<String> getName() {
        return name;
    }

    public List<String> getArtist() {
        return artist;
    }

    public List<String> getLink() {
        return link;
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

        // Parsing received JSON and saving access_token to String

        String json = response.body();
        JsonObject jo = JsonParser.parseString(json).getAsJsonObject();
        accessToken.append(jo.get("access_token").getAsString());
        System.out.println(accessToken);

    }

    public String spotifyRequestAuth(String apiPath) throws IOException, InterruptedException {

        // Creating authorization header

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(apiPath))
                .GET()
                .build();

        // Getting response

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();

    }

    public String spotifyRequestCategory(String apiPath, String categoryName) throws IOException, InterruptedException {

        // Parsing received JSON

        List<JsonObject> objects = new ArrayList<>();
        String json = spotifyRequestAuth(apiPath);
        JsonObject jo = JsonParser.parseString(json).getAsJsonObject();
        JsonObject categoriesObject = jo.get("categories").getAsJsonObject();
        JsonArray categoriesArray = categoriesObject.getAsJsonArray("items");

        for (int i = 0; i < categoriesArray.size(); i++) {
            objects.add(categoriesArray.get(i).getAsJsonObject());
        }

        // Printing data to user

        StringBuilder categoryID = new StringBuilder();
        name.clear();

        for (JsonObject object : objects) {
            if (categoryName == null) {
                name.add(object.get("name").getAsString());
            } else {
                if (categoryName.equals(object.get("name").getAsString())) {
                    categoryID.append(object.get("id").getAsString());
                    break;
                }
            }
        }

        return categoryID.toString();

    }

    public void spotifyRequestNew(String apiPath) throws IOException, InterruptedException {

        // Parsing received JSON

        List<JsonObject> objects = new ArrayList<>();
        String json = spotifyRequestAuth(apiPath);
        JsonObject jo = JsonParser.parseString(json).getAsJsonObject();
        JsonObject albums = jo.get("albums").getAsJsonObject();
        JsonArray newReleasesArray = albums.getAsJsonArray("items");

        for (int i = 0; i < newReleasesArray.size(); i++) {
            objects.add(newReleasesArray.get(i).getAsJsonObject());
        }

        // Printing data to user

        for (JsonObject object : objects) {

            // Getting URL data

            JsonObject externalUrl = object.get("external_urls").getAsJsonObject();

            // Getting artists data

            JsonArray artists = object.getAsJsonArray("artists");
            StringBuilder artistsString = new StringBuilder();
            List<JsonObject> artistsNames = new ArrayList<>();
            for (int i = 0; i < artists.size(); i++) {
                artistsNames.add(artists.get(i).getAsJsonObject());
            }
            artistsString.append("[");
            for (JsonObject artist : artistsNames) {
                artistsString.append(artist.get("name")).append(", ");
            }
            artistsString.replace(artistsString.length() - 2, artistsString.length(), "");
            artistsString.append("]");

            name.add(object.get("name").getAsString());
            artist.add(artistsString.toString().replace("\"", ""));
            link.add(externalUrl.get("spotify").toString().replace("\"", ""));

        }
    }

    public void spotifyRequestPlaylist(String apiPath) throws IOException, InterruptedException {

        // Parsing received JSON

        List<JsonObject> objects = new ArrayList<>();
        String json = spotifyRequestAuth(apiPath);
        JsonObject jo = JsonParser.parseString(json).getAsJsonObject();

        if (jo.toString().contains("error")) {

            JsonObject error = jo.get("error").getAsJsonObject();
            System.out.println();
            System.out.println(error.get("message").getAsString());

        } else {

            JsonObject playlists = jo.get("playlists").getAsJsonObject();
            JsonArray playlistArray = playlists.getAsJsonArray("items");

            for (int i = 0; i < playlistArray.size(); i++) {
                objects.add(playlistArray.get(i).getAsJsonObject());
            }

            // Printing data to user

            for (JsonObject object : objects) {

                // Getting URL data

                JsonObject externalUrl = object.get("external_urls").getAsJsonObject();

                name.add(object.get("name").getAsString());
                link.add(externalUrl.get("spotify").getAsString());

            }
        }
    }
}