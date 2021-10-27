
package ejb.session.stateless;

import entity.EmployeeEntity;
import entity.PartnerEntity;
import entity.RoomTypeEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerNotFoundException;
import util.exception.RoomTypeExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;


@Local
public interface HorsManagementControllerSessionBeanLocal {

    public Long createNewEmployee(EmployeeEntity newEmployeeEntity) throws UnknownPersistenceException, UsernameExistException;

    public EmployeeEntity employeeLogin(String username, String password) throws InvalidLoginCredentialException;

    public boolean EmployeeUsernameAlreadyExist(String username);

    public List<EmployeeEntity> retrieveAllEmployees();

    public Long createNewPartner(PartnerEntity newPartnerEntity) throws UsernameExistException, UnknownPersistenceException;

    public PartnerEntity retrievePartnerByUsername(String username) throws PartnerNotFoundException;

    public PartnerEntity partnerLogin(String username, String password) throws InvalidLoginCredentialException;

    public List<PartnerEntity> retrieveAllPartner();

    public Long createRoomType(RoomTypeEntity newRoomType) throws RoomTypeExistException, UnknownPersistenceException;
    
}
