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
import javax.ejb.Remote;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerNotFoundException;
import util.exception.RoomNotFoundException;
import util.exception.RoomTypeExistException;
import util.exception.RoomTypeNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;


@Remote
public interface HorsManagementControllerSessionBeanRemote {
    
     public Long createNewEmployee(EmployeeEntity newEmployeeEntity) throws UnknownPersistenceException, UsernameExistException;
     
     public EmployeeEntity employeeLogin(String username, String password) throws InvalidLoginCredentialException;
     
      public boolean EmployeeUsernameAlreadyExist(String username);
      
      public List<EmployeeEntity> retrieveAllEmployees();
      
       public Long createNewPartner(PartnerEntity newPartnerEntity) throws UsernameExistException, UnknownPersistenceException;
       
       public PartnerEntity retrievePartnerByUsername(String username) throws PartnerNotFoundException;
       
       public PartnerEntity partnerLogin(String username, String password) throws InvalidLoginCredentialException;
       
       public List<PartnerEntity> retrieveAllPartner();
       
       public Long createRoomType(RoomTypeEntity newRoomType) throws RoomTypeExistException, UnknownPersistenceException;
       
       public RoomTypeEntity retrieveRoomType(String name) throws RoomTypeNotFoundException;
       
       public void deleteRoomType(String name) throws RoomTypeNotFoundException;
}
