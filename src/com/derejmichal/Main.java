package com.derejmichal;

import java.io.IOException;

public class Main {

    static StringBuilder spotifyServer = new StringBuilder();

    public static void main(String[] args) throws IOException, InterruptedException {

        // Stuff needed for JetBrains Academy tests, will be changed later

        if (args.length == 0) {
            spotifyServer.append("https://accounts.spotify.com");
        }

        for (int i = 0; i < args.length; i += 2) {
            if ("-access".equals(args[i])) {
                spotifyServer.setLength(0);
                spotifyServer.append(args[i + 1]);
            } else {
                spotifyServer.setLength(0);
                spotifyServer.append("https://accounts.spotify.com");
            }
        }

        MusicAdvisor.setServer(spotifyServer);
        MusicAdvisor.menu();

    }
}