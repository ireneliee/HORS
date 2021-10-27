/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import entity.PartnerEntity;
import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import util.exception.EmployeeNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerNotFoundException;
import util.exception.RoomNotFoundException;
import util.exception.RoomNumberExistException;
import util.exception.RoomTypeExistException;
import util.exception.RoomTypeNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateRoomException;
import util.exception.UsernameExistException;


@Stateless
public class HorsManagementControllerSessionBean implements HorsManagementControllerSessionBeanRemote, 
        HorsManagementControllerSessionBeanLocal {

    @EJB
    private RoomEntitySessionBeanLocal roomEntitySessionBean;

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
    
    @Override
    public List<PartnerEntity> retrieveAllPartner() {
        return partnerEntitySessionBean.retrieveAllPartner();
    }
    
    @Override
    public Long createRoomType(RoomTypeEntity newRoomType) throws RoomTypeExistException, 
            UnknownPersistenceException {
        return roomTypeEntitySessionBean.createRoomType(newRoomType);
    }
    
    @Override
    public RoomTypeEntity retrieveRoomType(String name) throws RoomTypeNotFoundException{
        return roomTypeEntitySessionBean.retrieveRoomType(name);
    }
    
    @Override
    public void deleteRoomType(String name) throws RoomTypeNotFoundException{
         roomTypeEntitySessionBean.deleteRoomType(name);
    }
    
    @Override
    public void updateRoomType(RoomTypeEntity roomType) throws RoomTypeNotFoundException{
        roomTypeEntitySessionBean.updateRoomType(roomType);
    }
    
    @Override
    public List<RoomTypeEntity> retrieveAllRoomType(){
        return roomTypeEntitySessionBean.retrieveAllRoomType();
    }
    
    @Override
    public Long createNewRoom(RoomEntity newRoomEntity) throws RoomNumberExistException,
            UnknownPersistenceException, InputDataValidationException{
        return roomEntitySessionBean.createNewRoom(newRoomEntity);
    }
    
    @Override
     public RoomEntity retrieveRoomByRoomNumber(Integer roomNumber) throws RoomNotFoundException{
         return roomEntitySessionBean.retrieveRoomByRoomNumber(roomNumber);
     }
     
    @Override
      public void updateRoom(RoomEntity roomEntity) throws RoomNotFoundException,
            UpdateRoomException, InputDataValidationException {
          roomEntitySessionBean.updateRoom(roomEntity);
      }
      
    @Override
      public void deleteRoom(Integer roomNumber) throws RoomNotFoundException{
          roomEntitySessionBean.deleteRoom(roomNumber);
      }
   
        
            
    
  
    
}
