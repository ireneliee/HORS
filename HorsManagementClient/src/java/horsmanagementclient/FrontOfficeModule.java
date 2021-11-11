/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import ejb.session.stateless.HorsManagementControllerSessionBeanRemote;
import entity.EmployeeEntity;
import entity.PaymentEntity;
import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import util.enumeration.AccessRightEnum;
import util.enumeration.PaymentMethodEnum;
import util.exception.GuestHasNotCheckedInException;
import util.exception.InvalidAccessRightException;
import util.exception.InvalidRoomReservationEntityException;
import util.exception.LineItemExistException;
import util.exception.NoAvailableRoomOptionException;
import util.exception.NoMoreRoomToAccomodateException;
import util.exception.RateNotFoundException;
import util.exception.RoomTypeNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.WrongCheckInDate;
import util.exception.WrongCheckoutDate;
import util.reservation.Pair;

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
                    doSearchHotel();
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

  

    private void doReserve() {
    }

    private void formattingSearchRoom(RoomTypeEntity roomTypeEntity, Integer numberOfRooms) {
        System.out.print("Room type: " + roomTypeEntity.getName() + " ");
        System.out.println("Number of available room: " + numberOfRooms);
        System.out.println("**********************************************");
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

        } catch (InvalidRoomReservationEntityException | WrongCheckInDate | NoMoreRoomToAccomodateException ex) {
            System.out.println("An error has occured: " + ex.getMessage());
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
        } catch (InvalidRoomReservationEntityException | WrongCheckoutDate
                | GuestHasNotCheckedInException ex) {
            System.out.println("An error has occured: " + ex.getMessage());
        }
    }

    public LocalDate dateInput(String userInput) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate date = LocalDate.parse(userInput, dateFormat);

        return date;
    }
    
    public void doSearchHotel() {
        Scanner scanner = new Scanner(System.in);
        Integer cinDay = 0;
        Integer cinMonth = 0;
        Integer cinYear = 0;
        Integer coutDay = 0;
        Integer coutMonth = 0;
        Integer coutYear = 0;
        Integer numberOfRooms = 0;
        Integer i = 0;
        String confirmReserve = "";
        Integer option = 0;
        PaymentEntity newPaymentEntity = new PaymentEntity();
        
        
        System.out.println("*** HORS Reservation  :: Search Room ***\n");
        System.out.print("Enter Day of Check In> ");
        cinDay = scanner.nextInt();
        System.out.print("Enter Month of Check In> ");
        cinMonth = scanner.nextInt();
        System.out.print("Enter Year of Check In> ");
        cinYear = scanner.nextInt();
        System.out.print("Enter Day of Check Out> ");
        coutDay = scanner.nextInt();
        System.out.print("Enter Month of Check Out> ");
        coutMonth = scanner.nextInt();
        System.out.print("Enter Year of Check Out> ");
        coutYear = scanner.nextInt();
        System.out.print("Enter number of room(s)> ");
        numberOfRooms = scanner.nextInt();
        scanner.nextLine();
        
        try {
            LocalDate checkinDate = LocalDate.of(cinYear, cinMonth, cinDay);
            LocalDate checkoutDate = LocalDate.of(coutYear, coutMonth, coutDay);
            
            List<Pair> availableRooms = 
                    horsManagementControllerSessionBeanRemote.searchRoom(1, checkinDate, checkoutDate, numberOfRooms);
            System.out.printf("\n%3s%10s%10s", "No", "Room Type", "Total Price");
            
            for(Pair pair: availableRooms)
            {
                i++;
                System.out.printf("\n%3s%10s%10s", i, pair.getRoomType().getName(), pair.getPrice());
                
            }            
            
            System.out.println("");
            System.out.println("------------------------");
           
            System.out.print("Reserve Room(s)? (Enter 'Y' to reserve)> ");
            confirmReserve = scanner.nextLine().trim();

            if(confirmReserve.equals("Y"))
            {
                while(true) {

                    System.out.print("Select the option>");
                    option = scanner.nextInt();
                    if(option >= 1 || option <= availableRooms.size()) {

                        while(true)
                        {
                            System.out.println("The fee is $" + availableRooms.get(option - 1).getPrice() + ". Please choose the payment option");
                            System.out.println("1: AMEX");
                            System.out.println("2: Mastercard");
                            System.out.println("3: Visa");
                            System.out.print(">");
                            
                            Integer payment = scanner.nextInt();

                            if(payment >= 1 && payment <= 3)
                            {
                                newPaymentEntity.setPaymentMethod(PaymentMethodEnum.values()[payment - 1]);
                                newPaymentEntity.setAmountPaid(availableRooms.get(option - 1).getPrice());
                                
                                break;
                            }
                            else
                            {
                                System.out.println("Invalid option, please try again!\n");
                            }
                        }

                        Long reservationId = horsManagementControllerSessionBeanRemote.makeReservation(currentEmployeeEntity
                                , availableRooms, option-1, newPaymentEntity);
                        System.out.println("Reservation is successfully created. Please take note of your reservation id to check in: " +
                                reservationId);
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!");
                    }
                }

            }
            
        }
        catch (DateTimeException ex){
            System.out.println("Invalid Date input");
            
        }
        catch (NoAvailableRoomOptionException ex) {
            System.out.println("No room available");
        }
        catch (RoomTypeNotFoundException ex){
            System.out.println("Room type not available!");
        }
        catch (InvalidRoomReservationEntityException ex) {
            System.out.println("Invalid Reservation!");
        }
        catch (LineItemExistException ex){
            System.out.println("Invalid Reservation");
        }
        catch (UnknownPersistenceException ex) {
            System.out.println("Reservation failed");
        }
        
       
                
    }
}
