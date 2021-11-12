
package ejb.session.stateless;

import entity.EmployeeEntity;
import entity.NormalRateEntity;
import entity.PartnerEntity;
import entity.PaymentEntity;
import entity.PeakRateEntity;
import entity.PromotionRateEntity;
import entity.PublishedRateEntity;
import entity.RoomAllocationExceptionEntity;
import entity.RoomEntity;
import entity.RoomRateEntity;
import entity.RoomTypeEntity;
import entity.UserEntity;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeleteRoomRateException;
import util.exception.GuestHasNotCheckedInException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.InvalidRoomReservationEntityException;
import util.exception.LineItemExistException;
import util.exception.NoAvailableRoomOptionException;
import util.exception.NoMoreRoomToAccomodateException;
import util.exception.NormalRateHasAlreadyExistedException;
import util.exception.PartnerNotFoundException;
import util.exception.PeakRateHasAlreadyExistedException;
import util.exception.PromotionRateHasAlreadyExistedException;
import util.exception.PublishedRateHasAlreadyExistedException;
import util.exception.RoomAllocationExceptionReportDoesNotExistException;
import util.exception.RoomAllocationIsDoneException;
import util.exception.RoomNotFoundException;
import util.exception.RoomNumberExistException;
import util.exception.RoomRateEntityNotFoundException;
import util.exception.RoomTypeExistException;
import util.exception.RoomTypeHasBeenDisabledException;
import util.exception.RoomTypeNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateRoomException;
import util.exception.UsernameExistException;
import util.exception.WrongCheckInDate;
import util.exception.WrongCheckoutDate;
import util.reservation.Pair;


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

     public void updateRoomType(String roomTypeName, RoomTypeEntity newRoomType) throws RoomTypeNotFoundException;

    public List<RoomTypeEntity> retrieveAllRoomType();

    public Long createNewRoom(RoomEntity newRoomEntity) throws RoomNumberExistException,
            UnknownPersistenceException, InputDataValidationException, RoomTypeHasBeenDisabledException;

    public RoomEntity retrieveRoomByRoomNumber(String roomNumber) throws RoomNotFoundException;
    
     public void updateRoom(RoomEntity roomEntity) throws RoomNotFoundException,
            UpdateRoomException, InputDataValidationException;
     
     public void deleteRoom(String roomNumber) throws RoomNotFoundException;

    public List<RoomEntity> retrieveAllRooms();

    public Long createNewPublishedRateEntity(PublishedRateEntity newPublishedRateEntity) throws PublishedRateHasAlreadyExistedException, UnknownPersistenceException;

    public Long createNewNormalRateEntity(NormalRateEntity newNormalRateEntity) throws NormalRateHasAlreadyExistedException, UnknownPersistenceException;

    public Long createNewPeakRateEntity(PeakRateEntity newPeakRateEntity) throws PeakRateHasAlreadyExistedException, UnknownPersistenceException;

    public Long createNewPromotionRateEntity(PromotionRateEntity newPromotionRateEntity) throws PromotionRateHasAlreadyExistedException, UnknownPersistenceException;

    public RoomRateEntity retrieveRoomRateDetails(Long roomRateId) throws RoomRateEntityNotFoundException;
    
     public void updatePublishedAndNormalRate(RoomRateEntity roomRate);
    
    public void updatePromotionAndPeakRate(RoomRateEntity roomRate);
    
    public List<RoomRateEntity> retrieveAllRoomRate();
    
    public void deleteRoomRateEntity(RoomRateEntity roomRate) throws DeleteRoomRateException;

    public void allocateRoomGivenDate(LocalDate checkInDate) throws RoomAllocationIsDoneException;

    public RoomAllocationExceptionEntity retrieveReportByDate(LocalDate reportDate) throws RoomAllocationExceptionReportDoesNotExistException;

    public void checkOut(Long roomReservationId, LocalDate date) throws WrongCheckoutDate, InvalidRoomReservationEntityException,
            GuestHasNotCheckedInException;

    public List<RoomEntity> checkIn(Long roomReservationId, LocalDate date) throws InvalidRoomReservationEntityException, WrongCheckInDate, NoMoreRoomToAccomodateException;

    public List<Pair> searchRoom(int reserveType, LocalDate checkinDate, LocalDate checkoutDate, Integer numberOfRooms) throws NoAvailableRoomOptionException;

    public Long makeReservation(UserEntity username, List<Pair> roomResults, int response, PaymentEntity payment) throws RoomTypeNotFoundException, InvalidRoomReservationEntityException, LineItemExistException, UnknownPersistenceException;

    
}
