package org.example;


import java.sql.*;

public class TestDB {
    public static void main(String[] args) {

        try {
            Class.forName("org.postgresql.Driver");

            Connection connection =
                    DriverManager.getConnection(
                            // адрес БД , имя пользователя, пароль
                            "jdbc:postgresql://localhost:5432/demo","postgres","00000000");

            Statement statement = connection.createStatement();
            //Boolean result = statement.execute("create table.ftlh users(id bigint primary key, name varchar(50))");
            String sql = "select * from airplanes_data where airplane_code = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, "35X");
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getString("airplane_code"));
                System.out.println(resultSet.getString("model"));
                System.out.println(resultSet.getString("range"));
                System.out.println(resultSet.getString("speed"));
            }

            resultSet.close();


            statement.close();
            connection.close();


        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}