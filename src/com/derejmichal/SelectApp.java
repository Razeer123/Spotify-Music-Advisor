package com.derejmichal;

import java.sql.*;

public class SelectApp {

    private final String fileName;

    protected SelectApp(String fileName) {
        this.fileName = fileName;
    }

    private Connection connect() {

        String url = "jdbc:sqlite:" + fileName;
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;

    }

    public boolean login(String name) {

        boolean isUser = false;
        String sql = "SELECT name FROM spotify WHERE name=?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Setting parameter

            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            isUser = rs.next();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return isUser;
    }

    public String findID(String name) {

        String sql = "SELECT name, id FROM spotify WHERE name=?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Setting parameter

            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("id");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return "";

    }

    public String findSecret(String name) {

        String sql = "SELECT name, secret FROM spotify WHERE name=?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Setting parameter

            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("secret");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return "";

    }
}
