package com.example.oop.store;

import com.example.oop.DBManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Store {
    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        try (Connection conn = DBManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, name, price FROM items")) {

            while (rs.next()) {
                Item item = new Item(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price")
                );
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    /**
     * Можно добавить метод для добавления товара в базу.
     */
    public void addItem(String name, double price) {
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO items(name, price) VALUES(?, ?)")) {
            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
