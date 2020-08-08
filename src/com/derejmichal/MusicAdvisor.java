package com.derejmichal;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class MusicAdvisor {

    private static final StringBuilder spotifyServer = new StringBuilder();
    private static final StringBuilder apiServerPath = new StringBuilder();
    private static final AuthorizationApp app = new AuthorizationApp();
    private static final MusicAdvisorView view = new MusicAdvisorView();
    private static int resultPage;

    public static void setServer(StringBuilder server, StringBuilder apiPath, int resultPage) {
        spotifyServer.append(server);
        apiServerPath.append(apiPath);
        MusicAdvisor.resultPage = resultPage;
    }

    public static void menu(String providedChoice) throws IOException, InterruptedException {

        Scanner scanner = new Scanner(System.in);
        StringBuilder choice = new StringBuilder();
        boolean pageMenu;

        view.setResultPage(resultPage);

        while (!choice.toString().equals("exit")) {

            if (providedChoice == null) {
                choice.replace(0, choice.length(), scanner.nextLine());
            } else {
                choice.append(providedChoice);
            }

            providedChoice = null;

            StringBuilder category = new StringBuilder();

            if (choice.toString().contains("playlists")) {
                category.append(choice.substring(10, choice.length()));
            }
            String categoryTest = "playlists" + " " + category.toString();

            // Cannot use switch statement because of non-static variable

            int currentPage;
            if (choice.toString().equals("featured")) {

                if (app.isAuthorized()) {

                    System.out.println();
                    String url = apiServerPath.toString() + "/v1/browse/featured-playlists";
                    app.spotifyRequestPlaylist(url);
                    view.setName(app.getName());
                    view.setLink(app.getLink());
                    currentPage = 1;
                    view.display(currentPage, "featured");
                    pageMenu = true;

                    while (pageMenu) {
                        String pageMenuChoice = scanner.nextLine();
                        if (pageMenuChoice.equals("next")) {
                            if (currentPage < view.calculateSize()) {
                                currentPage++;
                                view.display(currentPage, "featured");
                            } else {
                                System.out.println("No more pages!");
                            }
                        } else if (pageMenuChoice.equals("prev")) {
                            if (currentPage > 1) {
                                currentPage--;
                                view.display(currentPage, "featured");
                            } else {
                                System.out.println("No more pages!");
                            }
                        } else {
                            pageMenu = false;
                            menu(pageMenuChoice);
                        }
                    }

                }
            } else if (choice.toString().equals("new")) {

                if (app.isAuthorized()) {

                    System.out.println();
                    String url = apiServerPath.toString() + "/v1/browse/new-releases";
                    app.spotifyRequestNew(url);
                    view.setName(app.getName());
                    view.setAuthor(app.getArtist());
                    view.setLink(app.getLink());
                    currentPage = 1;
                    view.display(currentPage, "new");
                    pageMenu = true;

                    while (pageMenu) {
                        String pageMenuChoice = scanner.nextLine();
                        if (pageMenuChoice.equals("next")) {
                            if (currentPage < view.calculateSize()) {
                                currentPage++;
                                view.display(currentPage, "new");
                            } else {
                                System.out.println("No more pages!");
                            }
                        } else if (pageMenuChoice.equals("prev")) {
                            if (currentPage > 1) {
                                currentPage--;
                                view.display(currentPage, "new");
                            } else {
                                System.out.println("No more pages!");
                            }
                        } else {
                            pageMenu = false;
                            menu(pageMenuChoice);
                        }
                    }

                }
            } else if (choice.toString().equals("categories")) {

                if (app.isAuthorized()) {

                    String url = apiServerPath.toString() + "/v1/browse/categories";
                    app.spotifyRequestCategory(url, null);
                    view.setName(app.getName());
                    currentPage = 1;
                    view.display(currentPage, "category");
                    pageMenu = true;

                    while (pageMenu) {
                        String pageMenuChoice = scanner.nextLine();
                        if (pageMenuChoice.equals("next")) {
                            if (currentPage < view.calculateSize()) {
                                currentPage++;
                                view.display(currentPage, "category");
                            } else {
                                System.out.println("No more pages!");
                            }
                        } else if (pageMenuChoice.equals("prev")) {
                            if (currentPage > 1) {
                                currentPage--;
                                view.display(currentPage, "display");
                            } else {
                                System.out.println("No more pages!");
                            }
                        } else {
                            pageMenu = false;
                            menu(pageMenuChoice);
                        }
                    }
                }
            } else if (choice.toString().equals(categoryTest)) {

                if (app.isAuthorized()) {

                    String categoryUrl = apiServerPath.toString() + "/v1/browse/categories?limit=50";
                    String categoryID = app.spotifyRequestCategory(categoryUrl, category.toString());
                    String url = apiServerPath.toString() + "/v1/browse/categories/" + categoryID + "/playlists";
                    app.spotifyRequestPlaylist(url);
                    view.setName(app.getName());
                    view.setLink(app.getLink());
                    currentPage = 1;
                    view.display(currentPage, "playlist");
                    pageMenu = true;

                    while (pageMenu) {
                        String pageMenuChoice = scanner.nextLine();
                        if (pageMenuChoice.equals("next")) {
                            if (currentPage < view.calculateSize()) {
                                currentPage++;
                                view.display(currentPage, "playlist");
                            } else {
                                System.out.println("No more pages!");
                            }
                        } else if (pageMenuChoice.equals("prev")) {
                            if (currentPage > 1) {
                                currentPage--;
                                view.display(currentPage, "playlist");
                            } else {
                                System.out.println("No more pages!");
                            }
                        } else {
                            pageMenu = false;
                            menu(pageMenuChoice);
                        }
                    }

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