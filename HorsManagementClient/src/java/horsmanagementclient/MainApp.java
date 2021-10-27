/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import ejb.session.stateless.HorsManagementControllerSessionBeanRemote;
import entity.EmployeeEntity;
import java.util.Scanner;
import util.exception.InvalidAccessRightException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author irene
 */
public class MainApp {
    
    private HorsManagementControllerSessionBeanRemote horsManagementControllerSessionBeanRemote;
    private EmployeeEntity currentEmployeeEntity;
    private SystemAdministrationModule systemAdministrationModule;
    private HotelOperationModule hotelOperationModule;
    
    public MainApp(){}

    public MainApp(HorsManagementControllerSessionBeanRemote horsManagementControllerSessionBeanRemote) {
            this.horsManagementControllerSessionBeanRemote = horsManagementControllerSessionBeanRemote;
    }
    
    public void runApp()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Welcome Hors Management System (v4.1) ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;
            
            while(response < 1 || response > 2)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    try
                    {
                        doLogin();
                        System.out.println("Login successful!\n");
                        hotelOperationModule = new HotelOperationModule(horsManagementControllerSessionBeanRemote, 
                                currentEmployeeEntity);
                        systemAdministrationModule = new SystemAdministrationModule(horsManagementControllerSessionBeanRemote, 
                                currentEmployeeEntity);
                        menuMain();
                    }
                    catch(InvalidLoginCredentialException ex) 
                    {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 2)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 2)
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
        
        System.out.println("*** HORS Management :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0)
        {
            currentEmployeeEntity = horsManagementControllerSessionBeanRemote.employeeLogin(username, password);
        }
        else
        {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }
   
   private void menuMain()
    {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Hors Management System ***\n");
            System.out.println("You are login as " + currentEmployeeEntity.getFirstName() + " " + 
                    currentEmployeeEntity.getLastName() + " with " + currentEmployeeEntity.getAccessRight().toString() + " rights\n");
            System.out.println("1: System Administration");
            System.out.println("2: Operation Manager");
            System.out.println("3: Sales Manager");
            System.out.println("4: Guest Relation Officerr");
            System.out.println("5: Logout\n");

            response = 0;
            
            while(response < 1 || response > 5)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    try{
                    systemAdministrationModule.menuSystemAdministration();
                    
                    } catch (InvalidAccessRightException ex) {
                        System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                    }
                }
                else if(response == 2)
                {

                    try
                    {
                        hotelOperationModule.operationManagerMenu();
                    }
                    catch (InvalidAccessRightException ex)
                    {
                        System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                    }

                }
                else if (response == 5)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 5)
            {
                break;
            }
        }
    }
    
    
    
}
