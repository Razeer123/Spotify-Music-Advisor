package com.derejmichal;

import java.io.IOException;
import java.util.Scanner;

public class MusicAdvisor {

    private static final StringBuilder spotifyServer = new StringBuilder();
    private static final StringBuilder apiServerPath = new StringBuilder();

    public static void setServer(StringBuilder server, StringBuilder apiPath) {
        spotifyServer.append(server);
        apiServerPath.append(apiPath);
    }

    public static void menu() throws IOException, InterruptedException {

        Scanner scanner = new Scanner(System.in);
        AuthorizationApp app = new AuthorizationApp();
        StringBuilder choice = new StringBuilder();

        while (!choice.toString().equals("exit")) {

            choice.replace(0, choice.length(), scanner.nextLine());
            StringBuilder category = new StringBuilder();
            if (choice.toString().contains("playlists")) {
                category.append(choice.substring(10, choice.length()));
            }
            String categoryTest = "playlists" + " " + category.toString();

            // Cannot use switch statement because of non-static variable

            if (choice.toString().equals("featured")) {

                if (app.isAuthorized()) {

                    System.out.println();
                    String url = apiServerPath.toString() + "/v1/browse/featured-playlists";
                    app.spotifyRequestPlaylist(url);

                }
            } else if (choice.toString().equals("new")) {

                if (app.isAuthorized()) {

                    System.out.println();
                    String url = apiServerPath.toString() + "/v1/browse/new-releases";
                    app.spotifyRequestNew(url);

                }
            } else if (choice.toString().equals("categories")) {

                if (app.isAuthorized()) {

                    System.out.println();
                    String url = apiServerPath.toString() + "/v1/browse/categories";
                    app.spotifyRequestCategory(url, null);

                }
            } else if (choice.toString().equals(categoryTest)) {

                if (app.isAuthorized()) {

                    String categoryUrl = apiServerPath.toString() + "/v1/browse/categories?limit=50";
                    String categoryID = app.spotifyRequestCategory(categoryUrl, category.toString());
                    String url = apiServerPath.toString() + "/v1/browse/categories/" + categoryID + "/playlists";
                    app.spotifyRequestPlaylist(url);

                }
            } else if (choice.toString().equals("auth")) {

                // Enter your Client ID here

                app.setServer(spotifyServer);
                System.out.println("use this link to request the access code: ");
                System.out.println(spotifyServer.toString() + "/authorize?" +
                        "client_id=" +
                        "&redirect_uri=http://localhost:8080&response_type=code");

                app.startServer();

                System.out.println();
                System.out.println("---SUCCESS---");
                boolean authorized = true;
                app.setAuthorized(authorized);

            } else if (choice.toString().equals("exit")) {

                System.out.println("---GOODBYE!---");
                System.exit(0);

            } else {

                System.out.println("---UNKNOWN COMMAND---");

            }
        }
    }
}