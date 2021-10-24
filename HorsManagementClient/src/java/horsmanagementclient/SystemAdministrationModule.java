/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import ejb.session.stateless.HorsManagementControllerSessionBeanRemote;
import entity.EmployeeEntity;
import java.util.List;
import java.util.Scanner;
import util.enumeration.AccessRightEnum;
import util.exception.InvalidAccessRightException;


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
            System.out.println("-----------------------");
            System.out.println("25: Back\n");
            response = 0;
            
            while(response < 1 || response > 25) {
                System.out.print("> ");
                response = sc.nextInt();
                
                if(response == 1) {
                    doCreateNewEmployee();
                } else if (response == 2) {
                    doViewAllStaff();
                } else if (response == 25) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if (response == 25) {
                break;
            }
        }
    }
    
    private void doCreateNewEmployee() {
        Scanner scanner = new Scanner(System.in);
        EmployeeEntity newEmployeeEntity = new EmployeeEntity();
        
        System.out.println("*** POS System :: System Administration :: Create New Employee***\n");
        System.out.print("Enter First Name> ");
        newEmployeeEntity.setFirstName(scanner.nextLine().trim());
        System.out.print("Enter Last Name> ");
        newEmployeeEntity.setLastName(scanner.nextLine().trim());
        System.out.print("Enter username>");
        String username = scanner.nextLine().trim();
        
        while(horsManagementControllerSessionBeanRemote.EmployeeUsernameAlreadyExist(username)){
            System.out.print("Enter username>");
            username = scanner.nextLine().trim();
        }
        
        newEmployeeEntity.setUsername(username);
        System.out.print("Enter password>");
        newEmployeeEntity.setPassword(scanner.nextLine());
        
         while(true)
        {
            System.out.println("Select Access Right (1: System administrator, 2: Operation Manager ");
            System.out.println("1: System administrator");
            System.out.println("2: Operation Manager");
            System.out.println("3: Sales Manager");
            System.out.println("4. Guest Relation Officer\n");
            Integer accessRightInt = scanner.nextInt();
            
            if(accessRightInt >= 1 && accessRightInt <= 4)
            {
                newEmployeeEntity.setAccessRight(AccessRightEnum.values()[accessRightInt-1]);
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
    
    
    
    
    
}
