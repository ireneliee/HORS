/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystemjavaseclient;

import java.util.List;
import java.util.Scanner;
import ws.client.InvalidLoginCredentialException;
import ws.client.InvalidLoginCredentialException_Exception;
import ws.client.PartnerEntity;
import ws.client.roomReservationEntityWebService.RoomReservationEntity;
import ws.client.roomReservationEntityWebService.ReservationNotFoundException_Exception;

/**
 *
 * @author zenyew
 */
public class MainApp {
    
    private PartnerEntity currentPartnerEntity;
    
    public void runApp(){
        
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome Hors Reservation System (v4.1) ***\n");
            System.out.println("1: Login");
            System.out.println("2: Search Hotel");
            System.out.println("3: Exit\n");
         
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
                    catch(InvalidLoginCredentialException_Exception ex) 
                    {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 2)
                {
                    doViewMyReservationDetails();
                }
                else if (response == 3)
                {
                    break;               
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 3)
            {
                break;
            }
        }
        
    }
    
    private void doLogin() throws InvalidLoginCredentialException_Exception 
    {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";
        
        System.out.println("*** HORS Reservation System :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0)
        {
            currentPartnerEntity = partnerLogin(username, password);
        }
        else
        {
            throw(new InvalidLoginCredentialException_Exception("Missing login credential!", new InvalidLoginCredentialException()));
        }
    }
    
    private void menuMain()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Hors Reservation System ***\n");
            System.out.println("You are login as " + currentPartnerEntity.getPartnerName());
            System.out.println("1: Search Hotel Room");
            System.out.println("2: View My Reservation Details");
            System.out.println("3: View All My Reservation");
            System.out.println("4: Logout\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    //search hotel room//
                }
                else if(response == 2)
                {
                    //view details//
                }
                else if (response == 3)
                {
                    doViewAllMyReservations();
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
    
    public void doViewMyReservationDetails() {
        
        Scanner scanner = new Scanner(System.in);
        Long reservationId = 0L;
       
        System.out.println("*** HORS Reservation  :: View My Reservation Details ***\n");
        System.out.print("Enter Reservation Id> ");
        reservationId = scanner.nextLong();
        
        try
        {
            RoomReservationEntity roomReservation = viewReservationDetails(reservationId);
            System.out.println("Reservation Id :" + roomReservation.getRoomReservationId());
            System.out.println("Booking Account :" + roomReservation.getBookingAccount());
            System.out.println("Reservation Date :" + roomReservation.getReservationDate());
            /*
            System.out.println("Number of Rooms :" + roomReservation.getRoomReservationLineItems().size());
            System.out.println("Check-in Date :" + roomReservation.getRoomReservationLineItems().get(0).getCheckInDate());
            System.out.println("Check-out Date :" + roomReservation.getRoomReservationLineItems().get(0).getCheckoutDate());
            */
            System.out.println("Total Amount :" + roomReservation.getTotalAmount());
            
                  
        } catch (ReservationNotFoundException_Exception ex) {
            System.out.println("Invalid Reservation Id");
        }
    }
    
    private void doViewAllMyReservations() {
        
        try 
        {
            List<RoomReservationEntity> reservations = viewAllMyReservations(currentPartnerEntity.getUserId());
        
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
                System.out.println("No reservation made");
            }
        } 
        /*catch (GuestNotFoundException_Exception ex) {
            System.out.println("Guest not found");
        }*/
        catch (ReservationNotFoundException_Exception ex) {
            System.out.println("No reservation made");
            
        }
                
    }
    
    private static PartnerEntity partnerLogin(java.lang.String username, java.lang.String password) throws ws.client.InvalidLoginCredentialException_Exception {
        ws.client.PartnerEntityWebService_Service service = new ws.client.PartnerEntityWebService_Service();
        ws.client.PartnerEntityWebService port = service.getPartnerEntityWebServicePort();
        return port.partnerLogin(username, password);
    }
    
    private static java.util.List<ws.client.roomReservationEntityWebService.RoomReservationEntity> viewAllMyReservations(java.lang.Long userId) throws ws.client.roomReservationEntityWebService.ReservationNotFoundException_Exception {
        ws.client.roomReservationEntityWebService.RoomReservationEntityWebService_Service service = new ws.client.roomReservationEntityWebService.RoomReservationEntityWebService_Service();
        ws.client.roomReservationEntityWebService.RoomReservationEntityWebService port = service.getRoomReservationEntityWebServicePort();
        return port.viewAllMyReservations(userId);
    }
    
    private static ws.client.roomReservationEntityWebService.RoomReservationEntity viewReservationDetails(java.lang.Long reservationId) throws ws.client.roomReservationEntityWebService.ReservationNotFoundException_Exception {
        ws.client.roomReservationEntityWebService.RoomReservationEntityWebService_Service service = new ws.client.roomReservationEntityWebService.RoomReservationEntityWebService_Service();
        ws.client.roomReservationEntityWebService.RoomReservationEntityWebService port = service.getRoomReservationEntityWebServicePort();
        return port.viewReservationDetails(reservationId);
    }
    
    
}
