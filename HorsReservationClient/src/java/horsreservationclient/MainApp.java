/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsreservationclient;


import ejb.session.stateful.ReserveOperationSessionBeanRemote;
import ejb.session.stateless.HorsReservationClientControllerRemote;
import entity.GuestEntity;
import entity.PaymentEntity;
import entity.RoomReservationEntity;
import entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import util.enumeration.PaymentMethodEnum;
import util.exception.GuestNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.InvalidRoomReservationEntityException;
import util.exception.ReservationNotFoundException;
import util.exception.RoomTypeNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;
import util.reservation.Pair;


public class MainApp {
    
    
    private HorsReservationClientControllerRemote horsReservationClientController;
    private GuestEntity currentGuestEntity;

    public MainApp() {
    }
    
   
    public MainApp(HorsReservationClientControllerRemote horsReservationClientController) {
        this.horsReservationClientController = horsReservationClientController;
       
    }
    
    
    public void runApp()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome Hors Reservation (v4.1) ***\n");
            System.out.println("1: Login");
            System.out.println("2: Register");
            System.out.println("3: Search Hotel Room");
            System.out.println("4: Exit\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    try
                    {
                        doLogin();
                        System.out.println("Login successful!\n");
                        menuMain();
                    }
                    catch(InvalidLoginCredentialException ex) 
                    {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 2)
                {
                    try
                    {
                        doRegister();
                       
                    }
                    catch(UsernameExistException ex) 
                    {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 3)
                {
                    //search room           
                }
                else if (response == 4)
                {
                    break;
                }
                else 
                {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if(response == 4)
            {
                break;
            }
        }
    }
    
    private void doLogin() throws InvalidLoginCredentialException
    {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("*** HORS Reservation  :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0)
        {
            currentGuestEntity = horsReservationClientController.guestLogin(username, password);
        }
        else
        {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }
    
    private void doRegister() throws UsernameExistException {
        Scanner scanner = new Scanner(System.in);
        GuestEntity newGuestEntity = new GuestEntity();
        
        System.out.println("*** HORS Reservation  :: Reservation***\n");
        System.out.print("Enter First Name> ");
        newGuestEntity.setFirstName(scanner.nextLine().trim());
        System.out.print("Enter Last Name> ");
        newGuestEntity.setLastName(scanner.nextLine().trim());
        System.out.print("Enter username>");
        String username = scanner.nextLine().trim();
        newGuestEntity.setUsername(username);
        System.out.print("Enter password>");
        newGuestEntity.setPassword(scanner.nextLine());
        System.out.print("Enter email>");
        newGuestEntity.setEmail(scanner.nextLine());
        System.out.print("Enter mobileNo>");
        newGuestEntity.setMobileNo(scanner.nextLine());
        System.out.print("Enter passportNo>");
        newGuestEntity.setPassportNo(scanner.nextLine());
        System.out.println();
        
        
        Long newGuestId = 0L;
            try{
                newGuestId = horsReservationClientController.guestRegister(newGuestEntity);
                System.out.println("Register successful!\n");
                System.out.println("A new guest with guestId " + newGuestId + " is created");
                System.out.println("Please login now!");
            } catch (UsernameExistException | UnknownPersistenceException ex) {
                System.out.println("Register failed!\n");
                System.out.println(ex.getMessage());
            }
            
                
              
    }
    
    
     private void menuMain()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Hors Reservation System ***\n");
            System.out.println("You are login as " + currentGuestEntity.getFirstName() + " " + 
                    currentGuestEntity.getLastName());
            System.out.println("1: Search Hotel Room");
            System.out.println("2: View My Reservation Details");
            System.out.println("3: View All My Reservation");
            System.out.println("4: Logout\n");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doSearchHotel();
                }
                else if(response == 2)
                {
                    doViewMyReservationDetails();
                }
                else if (response == 3)
                {
                    //view all
                }
                else if (response == 4)
                {
                    break;
                } 
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 4)
            {
                break;
            }
        }
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
            
            List<Pair> availableRooms = horsReservationClientController.searchRoom(4, checkinDate, checkoutDate, numberOfRooms);
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
                    
                    System.out.println("Select the option.");
                    option = scanner.nextInt();
                    if(option >= 1 || option <= availableRooms.size()) {

                        while(true)
                        {
                            System.out.println("The fee is $" + availableRooms.get(option - 1).getPrice() + ". Please choose the payment option");
                            System.out.println("1: AMEX; 2:MASTERCARD; 3:VISA");
                            Integer payment = scanner.nextInt();

                            if(payment >= 1 && payment <= 3)
                            {
                                newPaymentEntity.setPaymentMethod(PaymentMethodEnum.values()[payment - 1]);
                                break;
                            }
                            else
                            {
                                System.out.println("Invalid option, please try again!\n");
                            }
                        }

                        horsReservationClientController.makeReservation(currentGuestEntity, option-1, newPaymentEntity);
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
        catch (RoomTypeNotFoundException ex){
            System.out.println("Room type not available!");
        }
        catch (InvalidRoomReservationEntityException ex) {
            System.out.println("Invalid Reservation!");
        }
        
       
                
    }
     
    public void doViewMyReservationDetails() {
        
        Scanner scanner = new Scanner(System.in);
        Long reservationId = 0L;
       
        System.out.println("*** HORS Reservation  :: View My Reservation Details ***\n");
        System.out.print("Enter Reservation Id> ");
        reservationId = scanner.nextLong();
        
        try
        {
            RoomReservationEntity roomReservation = horsReservationClientController.viewReservationDetails(reservationId);
            System.out.println("Reservation Id :" + roomReservation.getRoomReservationId());
            System.out.println("Booking Account :" + roomReservation.getBookingAccount());
            System.out.println("Reservation Date :" + roomReservation.getReservationDate());
            /*
            System.out.println("Number of Rooms :" + roomReservation.getRoomReservationLineItems().size());
            System.out.println("Check-in Date :" + roomReservation.getRoomReservationLineItems().get(0).getCheckInDate());
            System.out.println("Check-out Date :" + roomReservation.getRoomReservationLineItems().get(0).getCheckoutDate());
            */
            System.out.println("Total Amount :" + roomReservation.getTotalAmount());
            
                  
        } catch (ReservationNotFoundException ex) {
            System.out.println("Invalid Reservation Id");
        }
    }
    
    public void viewAllReservations() throws  ReservationNotFoundException, GuestNotFoundException{
        
       
        try 
        {
            List<RoomReservationEntity> reservations = horsReservationClientController.viewAllReservation(currentGuestEntity.getUsername());
        
            System.out.printf("%10s%10s%10s%10s%10s%10s\n", "Reservation Id", "Booking Account", "Reservation Date", "Number of Rooms", "Check-in Date", "Check-out Date");
            if(reservations.isEmpty() == false) {
                for(RoomReservationEntity roomReservation : reservations) {
                    System.out.printf("%10s%10s%10s\n", roomReservation.getRoomReservationId(), roomReservation.getBookingAccount(), roomReservation.getReservationDate()
                                                                    /*,roomReservation.getRoomReservationLineItems().size(), roomReservation.getRoomReservationLineItems().get(0).getCheckInDate(),
                                                                    roomReservation.getRoomReservationLineItems().get(0).getCheckoutDate()*/);
                } 

            }
            else 
            {
                throw new ReservationNotFoundException("No reservation made");
            }
        } 
        catch (GuestNotFoundException ex) {
            System.out.println("Guest not found");
        }
                
    }

   
}

     

