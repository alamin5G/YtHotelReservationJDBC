package com.goonok.dao;

import com.mysql.cj.jdbc.exceptions.ConnectionFeatureNotAvailableException;

import java.sql.*;
import java.util.Scanner;

public class HotelDAO {
    private static final String url = "jdbc:mysql://localhost:3306/hoteldb";
    private static final String user = "root";
    private static final String pass = "252646";
    private Scanner scanner;
    private static Connection connection;

    public HotelDAO() {
        scanner = new Scanner(System.in);
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, pass);
        }catch (ClassNotFoundException c){
            System.err.println(c.getMessage());
        } catch (SQLException e) {
            System.err.println("Database Connection Failed - " + e.getMessage());
        }
    }

    public void reserveARoom(){
        try{
            System.out.print("Enter guest name: ");
            String guestName = scanner.nextLine();
            System.out.print("Enter room number: ");
            int roomNumber = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter Contact Number: ");
            String contactNumber = scanner.nextLine();
            System.out.println("====================================================");
            //SQL Query to insert the data while reserve a room for the guest;
            String sql = "INSERT INTO reservations (guest_name,room_number,contact_number) VALUES (?,?,?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, guestName);
            statement.setInt(2, roomNumber);
            statement.setString(3, contactNumber);
            int affectedRows = statement.executeUpdate();

            if (affectedRows>0){
                System.out.println(affectedRows + " room reserved for guest");
            }else {
                System.out.println("Failed to reserve a room for the guest");
            }
        }catch (SQLException s){
            System.err.println("Database Connection Failed - " + s.getMessage());
        }

    }

    public void viewReservedRoom()  {
        System.out.print("Enter customer Phone Number: ");
        String givenPhoneNumber = scanner.nextLine();

        String viewSql = "SELECT * FROM reservations WHERE contact_number = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(viewSql);
            preparedStatement.setString(1, givenPhoneNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("+----------------+------------+-------------+----------------+---------------------+");
            System.out.println("| reservation_id | guest_name | room_number | contact_number | reservation_date    |");
            System.out.println("+----------------+------------+-------------+----------------+---------------------+");
            while (resultSet.next()){
                int id = resultSet.getInt("reservation_id");
                String name = resultSet.getString("guest_name");
                int roomNumber = resultSet.getInt("room_number");
                String contactNumber = resultSet.getString("contact_number");
                String date = resultSet.getDate("reservation_date").toString();
                System.out.printf("| %-15s| %-11s| %-12s| %-15s| %-20s|", id, name, roomNumber, contactNumber, date);
                System.out.println("\n+----------------+------------+-------------+----------------+---------------------+");
            }
        } catch (SQLException e) {
            System.err.println("Connection failed on viewReservedRoom() - " +e.getMessage());
        }
    }

    public void getRoomNumber() {
        System.out.print("Enter reservation id: ");
        int reservationId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter reservation guest name: ");
        String guestName = scanner.nextLine();
        System.out.println("====================================================");
        String sql = "SELECT room_number from reservations WHERE reservation_id = ? AND guest_name = ?";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, reservationId);
            preparedStatement.setString(2, guestName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int roomNumber = resultSet.getInt("room_number");
                System.out.println("Room Number for Reservation ID " + reservationId +
                        " and Guest " + guestName + " is : " + roomNumber);
            }
        }catch (SQLException e){
            System.err.println("Connection Failed in getRoomNumber() - " + e.getMessage());
        }


    }

    public void updateReservation() {
        System.out.print("Enter reservation id: ");
        int reservationID = scanner.nextInt();
        if (isReservationExist(connection, reservationID)){
            scanner.nextLine();
            System.out.print("Enter guest name: ");
            String guestName = scanner.nextLine();
            System.out.print("Enter room number: ");
            int roomNumber = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter Contact Number: ");
            String contactNumber = scanner.nextLine();
            System.out.println("====================================================");
            String query = "UPDATE reservations SET guest_name = ?, room_number = ?, contact_number = ? WHERE reservation_id = ?";
            try{
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, guestName);
                preparedStatement.setInt(2, roomNumber);
                preparedStatement.setString(3, contactNumber);
                preparedStatement.setInt(4, reservationID);

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows>0){
                    System.out.println("Data updated for the guest id: " + reservationID);
                }else {
                    System.out.println("Failed to update the data for guest id: " + reservationID);
                }

            }catch (SQLException e){
                System.out.println("Database connection failed in updateReservation() - "+e.getMessage());
            }
        }else {
            System.out.println("Reservation not found for the given id");
        }
    }

    public void deleteReservation() {
    }

    private boolean isReservationExist(Connection connection, int reservationID){
        String findReservationIDQuery = "select * from reservations WHERE reservation_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(findReservationIDQuery);
            preparedStatement.setInt(1, reservationID);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            System.err.println("Database connection failed at isReservationExist() - " + e.getMessage());
        }
        return false;
    }
}
