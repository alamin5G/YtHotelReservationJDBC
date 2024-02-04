package com.goonok;

import com.goonok.dao.HotelDAO;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        HotelDAO hotelDAO = new HotelDAO();

        System.out.println("==========Welcome to Hotel Management System=========");
        while (true){
            Scanner input = new Scanner(System.in);
            System.out.println("1. Reserve a room");
            System.out.println("2. View Reserved room");
            System.out.println("3. Get room number");
            System.out.println("4. Update reservation");
            System.out.println("5. Delete reservation");
            System.out.println("0. For Exit");
            System.out.println("====================================================");
            System.out.print("Enter your choice: ");
            int choice = input.nextInt();
            switch (choice){
                case 1:
                    hotelDAO.reserveARoom();
                    System.out.println("====================================================");
                    break;
                case 2:
                    hotelDAO.viewReservedRoom();
                    System.out.println("====================================================");
                    break;
                case 3:
                    hotelDAO.getRoomNumber();
                    System.out.println("====================================================");
                    break;
                case 4:
                    hotelDAO.updateReservation();
                    System.out.println("====================================================");
                    break;
                case 5:
                    hotelDAO.deleteReservation();
                    System.out.println("====================================================");
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Enter valid choice");
                    break;
            }
        }

    }
}