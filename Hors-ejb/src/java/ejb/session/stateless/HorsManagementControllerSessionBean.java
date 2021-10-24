/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

/**
 *
 * @author irene
 */
@Stateless
public class HorsManagementControllerSessionBean implements HorsManagementControllerSessionBeanRemote, HorsManagementControllerSessionBeanLocal {

    @EJB
    private EmployeeEntitySessionBeanLocal employeeEntitySessionBean;
    
    
    
    public HorsManagementControllerSessionBean(){
        employeeEntitySessionBean = new EmployeeEntitySessionBean();
    }
    
    public Long createNewEmployee(EmployeeEntity newEmployeeEntity) throws UnknownPersistenceException, UsernameExistException {
        try {
            return employeeEntitySessionBean.createNewEmployee(newEmployeeEntity);
        } catch (UsernameExistException ex) {
            throw new UsernameExistException(ex.getMessage());
        } catch (UnknownPersistenceException ex) {
            throw new UnknownPersistenceException(ex.getMessage());
        }
    }
    
  
    
}
