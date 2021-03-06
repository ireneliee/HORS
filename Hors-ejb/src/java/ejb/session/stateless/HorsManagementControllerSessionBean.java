/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import ejb.session.stateful.ReserveOperationSessionBeanLocal;
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
import javax.ejb.EJB;
import javax.ejb.Stateless;
import util.exception.DeleteRoomRateException;
import util.exception.EmployeeNotFoundException;
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

@Stateless
public class HorsManagementControllerSessionBean implements HorsManagementControllerSessionBeanRemote,
        HorsManagementControllerSessionBeanLocal {

    @EJB
    private ReserveOperationSessionBeanLocal reserveOperationSessionBean;

    @EJB
    private RoomReservationEntitySessionBeanLocal roomReservationEntitySessionBean;

    @EJB
    private RoomAllocationSessionBeanLocal roomAllocationSessionBean;

    @EJB
    private RoomRateEntitySessionBeanLocal roomRateEntitySessionBean;

    @EJB
    private RoomEntitySessionBeanLocal roomEntitySessionBean;

    @EJB
    private RoomTypeEntitySessionBeanLocal roomTypeEntitySessionBean;

    @EJB
    private final PartnerEntitySessionBeanLocal partnerEntitySessionBean;

    @EJB
    private final EmployeeEntitySessionBeanLocal employeeEntitySessionBean;

    public HorsManagementControllerSessionBean() {
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
    public boolean EmployeeUsernameAlreadyExist(String username) {
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
    public RoomTypeEntity retrieveRoomType(String name) throws RoomTypeNotFoundException {
        return roomTypeEntitySessionBean.retrieveRoomType(name);
    }

    @Override
    public void deleteRoomType(String name) throws RoomTypeNotFoundException {
        roomTypeEntitySessionBean.deleteRoomType(name);
    }

    @Override
     public void updateRoomType(String roomTypeName, RoomTypeEntity newRoomType) throws RoomTypeNotFoundException {
        roomTypeEntitySessionBean.updateRoomType(roomTypeName, newRoomType);
    }

    @Override
    public List<RoomTypeEntity> retrieveAllRoomType() {
        return roomTypeEntitySessionBean.retrieveAllRoomType();
    }

    @Override
   public Long createNewRoom(RoomEntity newRoomEntity) throws RoomNumberExistException,
            UnknownPersistenceException, InputDataValidationException, RoomTypeHasBeenDisabledException {
        return roomEntitySessionBean.createNewRoom(newRoomEntity);
    }

    @Override
    public RoomEntity retrieveRoomByRoomNumber(String roomNumber) throws RoomNotFoundException {
        return roomEntitySessionBean.retrieveRoomByRoomNumber(roomNumber);
    }

    @Override
    public void updateRoom(RoomEntity roomEntity) throws RoomNotFoundException,
            UpdateRoomException, InputDataValidationException {
        roomEntitySessionBean.updateRoom(roomEntity);
    }

    @Override
    public void deleteRoom(String roomNumber) throws RoomNotFoundException {
        roomEntitySessionBean.deleteRoom(roomNumber);
    }

    @Override
    public List<RoomEntity> retrieveAllRooms() {
        return roomEntitySessionBean.retrieveAllRooms();
    }

    @Override
    public Long createNewPublishedRateEntity(PublishedRateEntity newPublishedRateEntity) throws PublishedRateHasAlreadyExistedException,
            UnknownPersistenceException {
        return roomRateEntitySessionBean.createNewPublishedRateEntity(newPublishedRateEntity);
    }

    @Override
    public Long createNewNormalRateEntity(NormalRateEntity newNormalRateEntity) throws NormalRateHasAlreadyExistedException,
            UnknownPersistenceException {
        return roomRateEntitySessionBean.createNewNormalRateEntity(newNormalRateEntity);
    }

    @Override
    public Long createNewPeakRateEntity(PeakRateEntity newPeakRateEntity) throws PeakRateHasAlreadyExistedException,
            UnknownPersistenceException {
        return roomRateEntitySessionBean.createNewPeakRateEntity(newPeakRateEntity);
    }

    @Override
    public Long createNewPromotionRateEntity(PromotionRateEntity newPromotionRateEntity) throws PromotionRateHasAlreadyExistedException,
            UnknownPersistenceException {
        return roomRateEntitySessionBean.createNewPromotionRateEntity(newPromotionRateEntity);
    }

    @Override
    public RoomRateEntity retrieveRoomRateDetails(Long roomRateId) throws RoomRateEntityNotFoundException {
        return roomRateEntitySessionBean.retrieveRoomRateDetails(roomRateId);
    }

    @Override
    public void updatePublishedAndNormalRate(RoomRateEntity roomRate) {
        roomRateEntitySessionBean.updatePublishedAndNormalRate(roomRate);
    }

    @Override
    public void updatePromotionAndPeakRate(RoomRateEntity roomRate) {
        roomRateEntitySessionBean.updatePromotionAndPeakRate(roomRate);
    }

    @Override
    public List<RoomRateEntity> retrieveAllRoomRate() {
        return roomRateEntitySessionBean.retrieveAllRoomRate();
    }

    @Override
    public void deleteRoomRateEntity(RoomRateEntity roomRate) throws DeleteRoomRateException {
        roomRateEntitySessionBean.deleteRoomRateEntity(roomRate);
    }

    @Override
    public void allocateRoomGivenDate(LocalDate checkInDate) throws RoomAllocationIsDoneException {
        roomAllocationSessionBean.allocateRoomGivenDate(checkInDate);
    }

    @Override
    public RoomAllocationExceptionEntity retrieveReportByDate(LocalDate reportDate) throws RoomAllocationExceptionReportDoesNotExistException {
        return roomAllocationSessionBean.retrieveReportByDate(reportDate);
    }

    @Override
    public void checkOut(Long roomReservationId, LocalDate date) throws WrongCheckoutDate, InvalidRoomReservationEntityException,
            GuestHasNotCheckedInException {
        roomReservationEntitySessionBean.checkOut(roomReservationId, date);
    }

    @Override
    public List<RoomEntity> checkIn(Long roomReservationId, LocalDate date) throws InvalidRoomReservationEntityException, WrongCheckInDate,
            NoMoreRoomToAccomodateException {
        return roomReservationEntitySessionBean.checkIn(roomReservationId, date);
    }

    @Override
    public List<Pair> searchRoom(int reserveType, LocalDate checkinDate, LocalDate checkoutDate, Integer numberOfRooms)  throws NoAvailableRoomOptionException{
        return reserveOperationSessionBean.searchRoom(reserveType, checkinDate, checkoutDate, numberOfRooms);
    }
    
    @Override
    public Long makeReservation(UserEntity username,List<Pair> roomResults,int response, PaymentEntity payment) throws RoomTypeNotFoundException, InvalidRoomReservationEntityException, LineItemExistException, UnknownPersistenceException {
        return reserveOperationSessionBean.makeReservation(username, roomResults, response, payment);
    }

}
