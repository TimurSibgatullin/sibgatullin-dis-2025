package org.example.Server.database;

import org.example.CommonFiles.Statistics;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatsRepository {

    private final Connection conn;

    public StatsRepository() throws SQLException {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:stats.db");
            init();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void init() throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.execute("""
                CREATE TABLE IF NOT EXISTS stats (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nickname TEXT,
                    session_date TEXT,
                    level INTEGER,
                    lifetime INTEGER,
                    kills INTEGER,
                    orbs INTEGER
                )
            """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void save(String nick, int level, long lifetime, int kills, int orbs) {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO stats(nickname, session_date, level, lifetime, kills, orbs) VALUES (?, datetime('now'), ?, ?, ?, ?)"
        )) {
            if (!nick.equals("")) {
                ps.setString(1, nick);
            } else {
                ps.setString(1, "Tank.io");
            }
            ps.setInt(2, level);
            ps.setLong(3, lifetime);
            ps.setInt(4, kills);
            ps.setInt(5, orbs);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized List<Statistics> top(int limit) {
        List<Statistics> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(
                """
                SELECT nickname, session_date, level, lifetime, kills, orbs
                FROM stats
                ORDER BY level DESC, lifetime DESC
                LIMIT ?
                """
        )) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Statistics(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getLong(4),
                        rs.getInt(5),
                        rs.getInt(6)
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(list);
        return list;
    }
}
