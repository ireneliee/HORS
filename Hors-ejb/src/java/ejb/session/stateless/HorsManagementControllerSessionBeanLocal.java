
package ejb.session.stateless;

import entity.EmployeeEntity;
import entity.NormalRateEntity;
import entity.PartnerEntity;
import entity.PeakRateEntity;
import entity.PromotionRateEntity;
import entity.PublishedRateEntity;
import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.NormalRateHasAlreadyExistedException;
import util.exception.PartnerNotFoundException;
import util.exception.PeakRateHasAlreadyExistedException;
import util.exception.PromotionRateHasAlreadyExistedException;
import util.exception.PublishedRateHasAlreadyExistedException;
import util.exception.RoomNotFoundException;
import util.exception.RoomNumberExistException;
import util.exception.RoomTypeExistException;
import util.exception.RoomTypeNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateRoomException;
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

    public RoomTypeEntity retrieveRoomType(String name) throws RoomTypeNotFoundException;

    public void deleteRoomType(String name) throws RoomTypeNotFoundException;

    public void updateRoomType(RoomTypeEntity roomType) throws RoomTypeNotFoundException;

    public List<RoomTypeEntity> retrieveAllRoomType();

    public Long createNewRoom(RoomEntity newRoomEntity) throws RoomNumberExistException, UnknownPersistenceException, InputDataValidationException;

    public RoomEntity retrieveRoomByRoomNumber(Integer roomNumber) throws RoomNotFoundException;
    
     public void updateRoom(RoomEntity roomEntity) throws RoomNotFoundException,
            UpdateRoomException, InputDataValidationException;
     
     public void deleteRoom(Integer roomNumber) throws RoomNotFoundException;

    public List<RoomEntity> retrieveAllRooms();

    public Long createNewPublishedRateEntity(PublishedRateEntity newPublishedRateEntity) throws PublishedRateHasAlreadyExistedException, UnknownPersistenceException;

    public Long createNewNormalRateEntity(NormalRateEntity newNormalRateEntity) throws NormalRateHasAlreadyExistedException, UnknownPersistenceException;

    public Long createNewPeakRateEntity(PeakRateEntity newPeakRateEntity) throws PeakRateHasAlreadyExistedException, UnknownPersistenceException;

    public Long createNewPromotionRateEntity(PromotionRateEntity newPromotionRateEntity) throws PromotionRateHasAlreadyExistedException, UnknownPersistenceException;
    
}
