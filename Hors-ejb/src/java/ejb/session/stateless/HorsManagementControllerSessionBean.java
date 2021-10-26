/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;


@Stateless
public class HorsManagementControllerSessionBean implements HorsManagementControllerSessionBeanRemote, 
        HorsManagementControllerSessionBeanLocal {

    @EJB
    private EmployeeEntitySessionBeanLocal employeeEntitySessionBean;
    
    public HorsManagementControllerSessionBean(){
        employeeEntitySessionBean = new EmployeeEntitySessionBean();
    }
    
    @Override
    public Long createNewEmployee(EmployeeEntity newEmployeeEntity) throws 
            UnknownPersistenceException, UsernameExistException {
        return employeeEntitySessionBean.createNewEmployee(newEmployeeEntity);
 
    }
    
    @Override
    public EmployeeEntity employeeLogin(String username, String password) 
            throws InvalidLoginCredentialException {
        return employeeEntitySessionBean.employeeLogin(username, password);
    }
    
    public boolean EmployeeUsernameAlreadyExist(String username){
        try {
            employeeEntitySessionBean.retrieveEmployeeByUsername(username);
        } catch (EmployeeNotFoundException ex) {
            return false;
        }
        
        return true;
        
    }
    
    public List<EmployeeEntity> retrieveAllEmployees() {
        return employeeEntitySessionBean.retrieveAllEmployees();
    }
    
  
    
}
