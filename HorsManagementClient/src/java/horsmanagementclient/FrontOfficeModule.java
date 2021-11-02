/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import ejb.session.stateless.HorsManagementControllerSessionBeanRemote;
import entity.EmployeeEntity;
import entity.RoomEntity;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import util.enumeration.AccessRightEnum;
import util.exception.InvalidAccessRightException;
import util.exception.InvalidRoomReservationEntityException;
import util.exception.WrongCheckInDate;
import util.exception.WrongCheckoutDate;

/**
 *
 * @author irene
 */
public class FrontOfficeModule {

    private HorsManagementControllerSessionBeanRemote horsManagementControllerSessionBeanRemote;
    private EmployeeEntity currentEmployeeEntity;

    public FrontOfficeModule() {
    }

    public FrontOfficeModule(HorsManagementControllerSessionBeanRemote horsManagementControllerSessionBeanRemote,
            EmployeeEntity currentEmployeeEntity) {
        this();
        this.horsManagementControllerSessionBeanRemote = horsManagementControllerSessionBeanRemote;
        this.currentEmployeeEntity = currentEmployeeEntity;
    }

    public void menuOfficeModule() throws InvalidAccessRightException {
        if (currentEmployeeEntity.getAccessRight() != AccessRightEnum.GUESTRELATIONOFFICER) {
            String errorMessage = "You don't have GUESTRELATIONOFFICER rights to access the system"
                    + " administration module. ";
            throw new InvalidAccessRightException(errorMessage);
        }
        Scanner sc = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** HORS Management System :: Front Office Module ***\n");
            System.out.println("1: Walk-in search room");
            System.out.println("2: Check-in guest");
            System.out.println("3: Check-out guest");
            System.out.println("4: Back");

            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = sc.nextInt();

                if (response == 1) {

                } else if (response == 2) {
                    doCheckIn();
                } else if (response == 3) {
                    doCheckout();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 4) {
                break;
            }
        }
    }

    public void doCheckIn() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** POS System :: Front Office Module :: Check-in ***\n");
        System.out.print("Enter room reservation Id stated in the booking> ");
        Long reservationId = Long.parseLong(scanner.nextLine().trim());

        System.out.print("Enter the date of today in the form of M/dd/yyyy>");
        String dateInString = scanner.nextLine().trim();
        LocalDate checkInDate = dateInput(dateInString);
        try {
            List<RoomEntity> listOfRooms = horsManagementControllerSessionBeanRemote.checkIn(reservationId, checkInDate);
            System.out.println("Here are your rooms: ");
            listOfRooms
                    .stream()
                    .forEach(System.out::println);
            
        } catch (InvalidRoomReservationEntityException | WrongCheckInDate ex) {
            System.out.println("An error has occured: "  +ex.getMessage());
        }

    }
    
    public void doCheckout() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** POS System :: Front Office Module :: Check-out ***\n");
        System.out.print("Enter room reservation Id stated in the booking> ");
        Long reservationId = Long.parseLong(scanner.nextLine().trim());

        System.out.print("Enter the date of today in the form of M/dd/yyyy>");
        String dateInString = scanner.nextLine().trim();
        LocalDate checkoutDate = dateInput(dateInString);
        try {
            horsManagementControllerSessionBeanRemote.checkOut(reservationId, checkoutDate);
            System.out.println("Check-out is successful!");
        } catch (InvalidRoomReservationEntityException | WrongCheckoutDate ex) {
            System.out.println("An error has occured: "  +ex.getMessage());
        }
    }

    public LocalDate dateInput(String userInput) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate date = LocalDate.parse(userInput, dateFormat);

        return date;
    }
}
