/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import ejb.session.stateless.HorsManagementControllerSessionBeanRemote;
import entity.EmployeeEntity;
import java.util.Scanner;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.AccessRightEnum;


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
    
    public void menuHotelOperation(){
        System.out.println("*** HORS Management System :: Hotel Operation ***\n");
         
        AccessRightEnum currentEmployeeAccessRight = currentEmployee.getAccessRight();
        
        if(currentEmployeeAccessRight.equals(AccessRightEnum.OPERATIONMANAGER)) {
            
            operationManagerMenu();
            
        } else if (currentEmployeeAccessRight.equals(AccessRightEnum.SALESMANAGER)) {
            
            salesManagerMenu();
            
        }
    }
    
    public void operationManagerMenu() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** HORS Management System :: Hotel Operation :: Operation Manager ***\n");
            System.out.println("1: Create new room type");

            System.out.println("9: Back\n");
            response = 0;
            
            while(response < 1 || response > 9)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {

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
    
    
 
    
    
    
    
}
