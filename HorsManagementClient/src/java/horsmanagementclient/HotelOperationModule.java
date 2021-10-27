/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import ejb.session.stateless.HorsManagementControllerSessionBeanRemote;
import entity.EmployeeEntity;
import entity.RoomTypeEntity;
import java.util.Scanner;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.AccessRightEnum;
import util.exception.InvalidAccessRightException;
import util.exception.RoomTypeExistException;
import util.exception.RoomTypeNotFoundException;
import util.exception.UnknownPersistenceException;


public class HotelOperationModule {
    private HorsManagementControllerSessionBeanRemote horsManagementControllerSessionBeanRemote;
    private EmployeeEntity currentEmployee;
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public HotelOperationModule(){
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public HotelOperationModule(HorsManagementControllerSessionBeanRemote horsManagementControllerSessionBeanRemote,
            EmployeeEntity currentEmployee) {
        this();
        this.horsManagementControllerSessionBeanRemote = horsManagementControllerSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }
    
    public void operationManagerMenu() throws InvalidAccessRightException {
        
        if(currentEmployee.getAccessRight() != AccessRightEnum.OPERATIONMANAGER) {
            String errorMessage = "You don't have OPERATIONMANAGER rights to access the system"
                    + " administration module. ";
            throw new InvalidAccessRightException(errorMessage);
        }
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** HORS Management System :: Hotel Operation :: Operation Manager ***\n");
            System.out.println("1: Create new room type");
            System.out.println("2: View room type details");

            System.out.println("9: Back\n");
            response = 0;
            
            while(response < 1 || response > 9)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doCreateNewRoomType();
                }
                else if(response == 2)
                {

                }
                else if(response == 3)
                {

                }
                else if(response == 9)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 9)
            {
                break;
            }
        }
    }
    
    public void salesManagerMenu(){}
    
    public void doViewRoomTypeDetails(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** HORS Management System :: Hotel Operation :: Operation Manager :: View Room Type Details ***\n");
        System.out.println("Enter the name of the room type you wish to delete> ");
        String nameOfRoomType = scanner.nextLine();
        
        try{
            horsManagementControllerSessionBeanRemote.deleteRoomType(nameOfRoomType);
        } catch (RoomTypeNotFoundException ex) {
            System.out.print("Error in retrieving room details: " + ex.getMessage());
        }
        
    }
    
    public void doCreateNewRoomType(){
        
        Scanner scanner = new Scanner(System.in);
        RoomTypeEntity newRoomType = new RoomTypeEntity();
        System.out.println("*** HORS Management System :: Hotel Operation :: Operation Manager :: Create New Room Type ***\n");
        
        
        System.out.print("Enter the name of the room type> ");
        newRoomType.setName(scanner.nextLine().trim());
        
        System.out.print("Enter any description about the room> ");
        newRoomType.setDescription(scanner.nextLine());
        
        System.out.print("Enter the size of the room (eg. 10x8) > ");
        newRoomType.setSize(scanner.nextLine().trim());
        
        System.out.print("Enter the number of bed>");
        newRoomType.setBed(Integer.parseInt(scanner.nextLine()));
        
        System.out.print("Enter capacity of the room (number of people the room can fit) >");
        newRoomType.setCapacity(Integer.parseInt(scanner.nextLine()));

        
        System.out.print("Enter the amenities in the room> ");
        newRoomType.setAmenities(scanner.nextLine());
        System.out.println();
        
        System.out.print("Enter the rank of the room (1 to 5) >");
        int rank = Integer.parseInt(scanner.nextLine());
        while(rank < 1 || rank > 5) {
            System.out.println("Rank is outside of the bound (1 - 5). Please enter another rank.");
            rank = Integer.parseInt(scanner.nextLine());
        }
        newRoomType.setRank(rank);
        
        try {
            Long newRoomTypeId = horsManagementControllerSessionBeanRemote.createRoomType(newRoomType);
            System.out.println("Room type with roomTypeId " + newRoomTypeId + " is created. ");
        } catch(RoomTypeExistException | UnknownPersistenceException ex) {
            System.out.println("Creation of room type failed: " + ex.getMessage());
        }
    }
        
        
    }
    
    
 
    
