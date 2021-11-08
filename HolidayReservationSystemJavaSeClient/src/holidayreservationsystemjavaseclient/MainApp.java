/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystemjavaseclient;

import java.util.Scanner;
import ws.client.InvalidLoginCredentialException;
import ws.client.InvalidLoginCredentialException_Exception;
import ws.client.PartnerEntity;

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
                   //search hotel
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
    
    private static PartnerEntity partnerLogin(String username, String password) throws InvalidLoginCredentialException_Exception {
        ws.client.PartnerEntityWebService_Service service = new ws.client.PartnerEntityWebService_Service();
        ws.client.PartnerEntityWebService port = service.getPartnerEntityWebServicePort();
        return port.partnerLogin(username, password);
    }
    
}
