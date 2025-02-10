package com.example.oop;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatService {

    public void sendMessage(String senderEmail, String recipientEmail, String content) {
        String sql = "INSERT INTO messages(senderEmail, recipientEmail, content) VALUES(?, ?, ?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, senderEmail);
            ps.setString(2, recipientEmail);
            ps.setString(3, content);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<String> getConversation(String userEmail, String adminEmail) {
        List<String> conversation = new ArrayList<>();
        String sql = """
            SELECT senderEmail, content, timestamp
            FROM messages
            WHERE (senderEmail = ? AND recipientEmail = ?)
               OR (senderEmail = ? AND recipientEmail = ?)
            ORDER BY id ASC
            """;
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userEmail);
            ps.setString(2, adminEmail);
            ps.setString(3, adminEmail);
            ps.setString(4, userEmail);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String sender = rs.getString("senderEmail");
                String text = rs.getString("content");
                String time = rs.getString("timestamp");
                conversation.add("[" + time + "] " + sender + ": " + text);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conversation;
    }
}
