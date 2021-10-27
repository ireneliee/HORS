/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import entity.PartnerEntity;
import entity.RoomTypeEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerNotFoundException;
import util.exception.RoomTypeExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;


@Stateless
public class HorsManagementControllerSessionBean implements HorsManagementControllerSessionBeanRemote, 
        HorsManagementControllerSessionBeanLocal {

    @EJB
    private RoomTypeEntitySessionBeanLocal roomTypeEntitySessionBean;

    @EJB
    private final PartnerEntitySessionBeanLocal partnerEntitySessionBean;

    @EJB
    private final EmployeeEntitySessionBeanLocal employeeEntitySessionBean;
    
    public HorsManagementControllerSessionBean(){
        employeeEntitySessionBean = new EmployeeEntitySessionBean();
        partnerEntitySessionBean = new PartnerEntitySessionBean();
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
    
    @Override
    public boolean EmployeeUsernameAlreadyExist(String username){
        try {
            employeeEntitySessionBean.retrieveEmployeeByUsername(username);
        } catch (EmployeeNotFoundException ex) {
            return false;
        }
        
        return true;
        
    }
    
    @Override
    public List<EmployeeEntity> retrieveAllEmployees() {
        return employeeEntitySessionBean.retrieveAllEmployees();
    }
    
    @Override
    public Long createNewPartner(PartnerEntity newPartnerEntity) throws UsernameExistException,
            UnknownPersistenceException {
        return partnerEntitySessionBean.createNewPartner(newPartnerEntity);
    }
    
    @Override
    public PartnerEntity retrievePartnerByUsername(String username) throws PartnerNotFoundException {
        return partnerEntitySessionBean.retrievePartnerByUsername(username);
    }
    
    @Override
    public PartnerEntity partnerLogin(String username, String password) throws InvalidLoginCredentialException {
        return partnerEntitySessionBean.partnerLogin(username, password);
    }
    
    public List<PartnerEntity> retrieveAllPartner() {
        return partnerEntitySessionBean.retrieveAllPartner();
    }
    
    public Long createRoomType(RoomTypeEntity newRoomType) throws RoomTypeExistException, 
            UnknownPersistenceException {
        return roomTypeEntitySessionBean.createRoomType(newRoomType);
    }
        
            
    
  
    
}
