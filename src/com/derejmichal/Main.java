package com.derejmichal;

import java.io.IOException;

public class Main {

    static StringBuilder spotifyServer = new StringBuilder();
    static StringBuilder apiServerPath = new StringBuilder();

    public static void main(String[] args) throws IOException, InterruptedException {

        // Stuff needed for JetBrains Academy tests, will be changed later

        if (args.length == 0) {
            spotifyServer.append("https://accounts.spotify.com");
            apiServerPath.append("https://api.spotify.com");
        }

        for (int i = 0; i < args.length; i += 2) {
            if ("-access".equals(args[i])) {
                spotifyServer.setLength(0);
                spotifyServer.append(args[i + 1]);
                break;
            } else {
                spotifyServer.setLength(0);
                spotifyServer.append("https://accounts.spotify.com");
            }
        }

        for (int i = 2; i < args.length; i += 2) {
            if ("-resource".equals(args[i])) {
                apiServerPath.setLength(0);
                apiServerPath.append(args[i + 1]);
                break;
            } else {
                apiServerPath.setLength(0);
                apiServerPath.append("https://api.spotify.com");
            }
        }

        MusicAdvisor.setServer(spotifyServer, apiServerPath);
        MusicAdvisor.menu();

    }
}