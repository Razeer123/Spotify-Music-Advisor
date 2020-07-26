package com.derejmichal;

import java.io.IOException;
import java.util.Scanner;

public class MusicAdvisor {

    private static StringBuilder spotifyServer = new StringBuilder();

    public static void setServer(StringBuilder server) {
        spotifyServer.append(server);
    }

    public static void menu() throws IOException, InterruptedException {

        Scanner scanner = new Scanner(System.in);
        AuthorizationApp app = new AuthorizationApp();
        StringBuilder choice = new StringBuilder();

        while (!choice.toString().equals("exit")) {

            choice.replace(0, choice.length(), scanner.nextLine());

            switch (choice.toString()) {

                case "featured":

                    if (app.isAuthorized()) {

                        System.out.println("---FEATURED---");
                        System.out.println("Mellow Morning");
                        System.out.println("Wake Up and Smell the Coffee");
                        System.out.println("Monday Motivation");
                        System.out.println("Songs to Sing in the Shower");

                    }

                    break;

                case "new":

                    if (app.isAuthorized()) {

                        System.out.println("---NEW RELEASES---");
                        System.out.println("Mountains [Sia, Diplo, Labrinth]");
                        System.out.println("Runaway [Lil Peep]");
                        System.out.println("The Greatest Show [Panic! At The Disco]");
                        System.out.println("All Out Life [Slipknot]");

                    }

                    break;

                case "categories":

                    if (app.isAuthorized()) {

                        System.out.println("---CATEGORIES---");
                        System.out.println("Top Lists");
                        System.out.println("Pop");
                        System.out.println("Mood");
                        System.out.println("Latin");
                    }

                    break;

                case "playlists Mood":

                    if (app.isAuthorized()) {

                        System.out.println("---MOOD PLAYLISTS---");
                        System.out.println("Walk Like A Badass");
                        System.out.println("Rage Beats");
                        System.out.println("Arab Mood Booster");
                        System.out.println("Sunday Stroll");

                    }

                    break;

                case "auth":

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

                    break;

                case "exit":

                    System.out.println("---GOODBYE!---");
                    System.exit(0);
                    break;

                default:

                    System.out.println("---UNKNOWN COMMAND---");
                    break;

            }
        }
    }
}
