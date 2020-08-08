package com.derejmichal;

import java.io.IOException;
import java.sql.*;

public class Main {

    static StringBuilder spotifyServer = new StringBuilder();
    static StringBuilder apiServerPath = new StringBuilder();
    static int resultPage;
    static String fileName;

    public static void main(String[] args) throws IOException, InterruptedException {

        fileName = "userData.db";

        // Default initialization of server url, api path etc.

        if (args.length == 0) {
            spotifyServer.append("https://accounts.spotify.com");
            apiServerPath.append("https://api.spotify.com");
            resultPage = 5;
        }

        // Way to initialize data explained above using command line arguments:
        // -access will initialize address of Spotify server
        // -resource will initialize address of Spotify Web API
        // -page will set, how many answers should appear on a page of the list

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

        for (int i = 4; i < args.length; i += 2) {
            if ("-page".equals(args[i])) {
                resultPage = Integer.parseInt(args[i + 1]);
                break;
            } else {
                resultPage = 5;
            }
        }

        System.out.println("Hello! Type 'help' or 'h' to see available commands.");

        // Creating an SQL database using functions

        createDatabase(fileName);
        createTable(fileName);

        MusicAdvisor.setServer(spotifyServer, apiServerPath, resultPage);
        MusicAdvisor.menu(null);

    }

    public static void createDatabase(String fileName) {

        // Connection string

        String url = "jdbc:sqlite:" + fileName;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createTable(String fileName) {

        // Connection string

        String url = "jdbc:sqlite:" + fileName;

        // Creating table

        String sql = "CREATE TABLE IF NOT EXISTS spotify (" +
                " number INTEGER NOT NULL PRIMARY KEY," +
                " name TEXT NOT NULL," +
                " id TEXT NOT NULL," +
                " secret TEXT NOT NULL" +
                ");";

        try (Connection conn = DriverManager.getConnection(url)) {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
