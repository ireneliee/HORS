/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsreservationclient;


import ejb.session.stateless.HorsReservationClientControllerRemote;
import entity.GuestEntity;
import java.util.Scanner;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;


public class MainApp {
    
    private HorsReservationClientControllerRemote horsReservationClientController;
    private GuestEntity currentGuestEntity;

    public MainApp() {
    }
    
   
    public MainApp(HorsReservationClientControllerRemote horsReservationClientControllerr) {
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
                        System.out.println("Register successful!\n");
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
            } catch (UsernameExistException | UnknownPersistenceException ex) {
                System.out.println(ex.getMessage());
            }
            System.out.println("A new guest with guestId " + newGuestId + " is created");
                
              
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
}
