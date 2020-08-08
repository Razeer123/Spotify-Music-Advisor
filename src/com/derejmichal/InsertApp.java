package com.derejmichal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertApp {

    private final String fileName;

    protected InsertApp(String fileName) {
        this.fileName = fileName;
    }

    private Connection connect () {

        String url = "jdbc:sqlite:" + fileName;
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public void insert (String name, String id, String secret) {

        String sql = "INSERT INTO spotify (name,id,secret) VALUES(?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Setting parameters

            pstmt.setString(1, name);
            pstmt.setString(2, id);
            pstmt.setString(3, secret);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
