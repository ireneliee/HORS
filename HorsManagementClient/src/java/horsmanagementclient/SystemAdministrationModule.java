/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import ejb.session.stateless.HorsManagementControllerSessionBeanRemote;
import entity.EmployeeEntity;
import entity.PartnerEntity;
import java.util.List;
import java.util.Scanner;
import util.enumeration.AccessRightEnum;
import util.exception.InvalidAccessRightException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;


public class SystemAdministrationModule {
    private HorsManagementControllerSessionBeanRemote horsManagementControllerSessionBeanRemote;
    private EmployeeEntity currentEmployeeEntity;
    
    private SystemAdministrationModule(){}

    public SystemAdministrationModule(HorsManagementControllerSessionBeanRemote horsManagementControllerSessionBeanRemote,
            EmployeeEntity currentEmployeeEntity) {
        this();
        this.horsManagementControllerSessionBeanRemote = horsManagementControllerSessionBeanRemote;
        this.currentEmployeeEntity = currentEmployeeEntity;
    }
    
    public void menuSystemAdministration() throws InvalidAccessRightException {
        if(currentEmployeeEntity.getAccessRight() != AccessRightEnum.SYSTEMADMINISTRATOR) {
            String errorMessage = "You don't have SYSTEMADMINISTRATOR rights to access the system"
                    + " administration module. ";
            throw new InvalidAccessRightException(errorMessage);
        }
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** HORS Management System :: System Administration ***\n");
            System.out.println("1: Create New Staff");
            System.out.println("2: View All Staffs");
            System.out.println("3: Create New Partner");
            System.out.println("4: View All Partners");
            System.out.println("-----------------------");
            System.out.println("5: Back\n");
            response = 0;
            
            while(response < 1 || response > 25) {
                System.out.print("> ");
                response = sc.nextInt();
                
                if(response == 1) {
                    doCreateNewEmployee();
                } else if (response == 2) {
                    doViewAllStaff();
                } else if(response == 3) {
                    doCreateNewPartner();
                } else if(response == 4) {
                    doViewAllPartners();
                }else if (response == 5) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if (response == 5) {
                break;
            }
        }
    }
    
    private void doCreateNewEmployee() {
        Scanner scanner = new Scanner(System.in);
        EmployeeEntity newEmployeeEntity = new EmployeeEntity();
        System.out.println("*** HORS Management System :: System Administration :: Create New Employee***\n");
        System.out.print("Enter First Name> ");
        newEmployeeEntity.setFirstName(scanner.nextLine().trim());
        System.out.print("Enter Last Name> ");
        newEmployeeEntity.setLastName(scanner.nextLine().trim());
        System.out.print("Enter username>");
        String username = scanner.nextLine().trim();
        newEmployeeEntity.setUsername(username);
        System.out.print("Enter password>");
        newEmployeeEntity.setPassword(scanner.nextLine());
        System.out.println();
        
         while(true)
        {
            System.out.println("Select Access Right: ");
            System.out.println("1: System administrator");
            System.out.println("2: Operation Manager");
            System.out.println("3: Sales Manager");
            System.out.println("4. Guest Relation Officer\n");
            System.out.print(">");
            Integer accessRightInt = scanner.nextInt();
            
            if(accessRightInt >= 1 && accessRightInt <= 4)
            {
                newEmployeeEntity.setAccessRight(AccessRightEnum.values()[accessRightInt-1]);
                Long newEmployeeId = 0L;
                try{
                    newEmployeeId = horsManagementControllerSessionBeanRemote.createNewEmployee(newEmployeeEntity);
                } catch (UsernameExistException | UnknownPersistenceException ex) {
                    System.out.println(ex.getMessage());
                    System.out.println("A new employee with employeeId " + newEmployeeId + " is created");
                }
                
                
                break;
            }
            else
            {
                System.out.println("Invalid option, please try again!\n");
            }
        }
        
    }
    
    private void doViewAllStaff(){
        List<EmployeeEntity> employeeEntities = horsManagementControllerSessionBeanRemote.retrieveAllEmployees();
        employeeEntities
                .stream()
                .forEach(employee -> System.out.printf("%8s%20s%20s%40s\n", employee.getUserId(),
                        employee.getFirstName(), employee.getLastName(), employee.getAccessRight()));
                
    }
    
    private void doCreateNewPartner() {
        Scanner scanner  = new Scanner(System.in);
        PartnerEntity newPartnerEntity = new PartnerEntity();
        
        System.out.println("*** POS System :: System Administration :: Create New Partner ***\n");
        System.out.print("Enter Partner Name> ");
        newPartnerEntity.setPartnerName(scanner.nextLine().trim());
        System.out.print("Enter Username> ");
        newPartnerEntity.setUsername(scanner.nextLine().trim());
        System.out.print("Enter Password> ");
        newPartnerEntity.setPassword(scanner.nextLine().trim());
        
        try{
            Long newPartnerEntityId = horsManagementControllerSessionBeanRemote.createNewPartner(newPartnerEntity);
            System.out.println("New partner with partnerId of " + newPartnerEntityId + " is created.");
        } catch (UsernameExistException | UnknownPersistenceException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
     private void doViewAllPartners(){
        List<PartnerEntity> employeeEntities = horsManagementControllerSessionBeanRemote.retrieveAllPartner();
        employeeEntities
                .stream()
                .forEach(employee -> System.out.printf("%8s%20s\n", employee.getUserId(),
                        employee.getPartnerName()));
                
    }
    
    
    
    
    
    
    
}
