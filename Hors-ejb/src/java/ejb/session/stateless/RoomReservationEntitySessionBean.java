/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import ejb.session.stateful.ReserveOperationSessionBeanLocal;
import entity.PaymentEntity;
import entity.RoomEntity;
import entity.RoomReservationEntity;
import entity.RoomReservationLineItemEntity;
import entity.RoomTypeEntity;
import entity.UserEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.enumeration.PaymentMethodEnum;
import util.exception.GuestHasNotCheckedInException;
import util.exception.InvalidRoomReservationEntityException;
import util.exception.LineItemExistException;
import util.exception.NoMoreRoomToAccomodateException;
import util.exception.ReservationNotFoundException;
import util.exception.RoomTypeNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.WrongCheckInDate;
import util.exception.WrongCheckoutDate;

@Stateless
public class RoomReservationEntitySessionBean implements RoomReservationEntitySessionBeanRemote, RoomReservationEntitySessionBeanLocal {

    @EJB(name = "RoomTypeEntitySessionBeanLocal")
    private RoomTypeEntitySessionBeanLocal roomTypeEntitySessionBeanLocal;

    @EJB
    private RoomAllocationSessionBeanLocal roomAllocationSessionBean;
    
    @Resource
    private EJBContext eJBContext;

    @PersistenceContext(unitName = "Hors-ejbPU")
    private EntityManager em;

    public RoomReservationEntitySessionBean() {
    }

    // for one that needs user entity, does not include guest
    // payment must be added before -> then can create roomReservationEntity
    // roomReservationLineItemEntity must be added before too -> then can create room reservation entity
    @Override
    public RoomReservationEntity createNewRoomReservationEntity(Long userId,
            RoomReservationEntity newRoomReservationEntity) throws InvalidRoomReservationEntityException {

        if (newRoomReservationEntity == null) {
            System.out.println("Reach C");
            throw new InvalidRoomReservationEntityException("Room reservation information not provided.");

        }
        UserEntity user = em.find(UserEntity.class, userId);

        user.getRoomReservations().add(newRoomReservationEntity);

        newRoomReservationEntity.setBookingAccount(user);
        System.out.println("Reach D");

        em.persist(newRoomReservationEntity);
        System.out.println("Reach C");

        em.flush();

        allocateRoomNow(newRoomReservationEntity);

        return newRoomReservationEntity;

    }

    private void allocateRoomNow(RoomReservationEntity newRoomReservationEntity) {
        List<RoomReservationLineItemEntity> roomReservations = newRoomReservationEntity.getRoomReservationLineItems();
        RoomReservationLineItemEntity firstReservation = roomReservations.get(0);
        LocalDate checkinDate = firstReservation.getCheckInDate();

        if (newRoomReservationEntity.getReservationDate().isEqual(checkinDate)) {
            LocalDateTime timeNow = LocalDateTime.now();
            LocalTime hourNow = LocalTime.of(2, 0);
            LocalDateTime twoAmMark = LocalDateTime.of(checkinDate, hourNow);
            if (timeNow.isAfter(twoAmMark)) {
                roomAllocationSessionBean.allocateRoomNow(newRoomReservationEntity);
            }
        }
    }

    @Override
    public Long createNewRoomReservationEntity(RoomReservationEntity newRoomReservationEntity) throws InvalidRoomReservationEntityException {

        if (newRoomReservationEntity == null) {

            throw new InvalidRoomReservationEntityException("Room reservation information not provided.");

        }

        em.persist(newRoomReservationEntity);

        return newRoomReservationEntity.getRoomReservationId();

    }

    @Override
    public List<RoomEntity> checkIn(Long roomReservationId, LocalDate date) throws InvalidRoomReservationEntityException, WrongCheckInDate,
            NoMoreRoomToAccomodateException {

        try {
            RoomReservationEntity roomReservation = em.find(RoomReservationEntity.class, roomReservationId);
            List<RoomReservationLineItemEntity> listOfRoomReservations = roomReservation.getRoomReservationLineItems();

            if (!listOfRoomReservations.get(0).getCheckInDate().equals(date)) {
                throw new WrongCheckInDate("You are not supposed to check in today!");
            }
            List<RoomEntity> roomsForCheckIn = new ArrayList<>();
            // checkIn = true
            for (RoomReservationLineItemEntity x : listOfRoomReservations) {
                if (x.getRoomAllocation() != null) {
                    roomsForCheckIn.add(x.getRoomAllocation());
                    x.setCheckedIn(true);
                } else {
                    String roomNumber = "";
                    for (int i = 0; i < roomsForCheckIn.size(); i++) {
                        roomNumber = roomNumber + roomsForCheckIn.get(i) + "\n";
                    }
                    throw new NoMoreRoomToAccomodateException("Currently, the hotel only has " + roomsForCheckIn.size() + " room(s)"
                            + " to accomodate for the guest's reservation. The room(s) are \n" + roomNumber + "\nPlease handle this manually");
                }
            }

            return roomsForCheckIn;

        } catch (NoResultException ex) {
            throw new InvalidRoomReservationEntityException("Room reservation does not exist.");
        }

    }

    @Override
    public void checkOut(Long roomReservationId, LocalDate date) throws WrongCheckoutDate, InvalidRoomReservationEntityException,
            GuestHasNotCheckedInException {
        try {
            RoomReservationEntity roomReservation = em.find(RoomReservationEntity.class, roomReservationId);
            List<RoomReservationLineItemEntity> listOfRoomReservations = roomReservation.getRoomReservationLineItems();
            // check if the guest has checked in
            if (!listOfRoomReservations.get(0).getCheckedIn()) {
                throw new GuestHasNotCheckedInException("Guest has not checked in!'");
            }
            if (!(listOfRoomReservations.get(0).getCheckoutDate().equals(date))) {
                System.out.println(listOfRoomReservations.get(0).getCheckoutDate());
                throw new WrongCheckoutDate("Checkout date need to be rechecked!");
            }

            // checkIn = true
            listOfRoomReservations
                    .stream()
                    .forEach(x -> x.setCheckedOut(true));

        } catch (NoResultException ex) {
            throw new InvalidRoomReservationEntityException("Room reservation does not exist.");
        }
    }

    @Override
    public List<RoomReservationEntity> viewAllMyReservation(Long userId) throws ReservationNotFoundException{

        String databaseQueryString = "SELECT rr FROM RoomReservationEntity rr WHERE rr.bookingAccount.userId = :iUserId";
        Query query = em.createQuery(databaseQueryString);
        query.setParameter("iUserId", userId);

        try{
        List<RoomReservationEntity> reservations = query.getResultList();

        return reservations;
        }
        catch(NoResultException ex) {
            throw new ReservationNotFoundException("No reservation made");
                    
        }
    }

    @Override
    public RoomReservationEntity viewReservationDetails(Long reservationId) throws ReservationNotFoundException {
        RoomReservationEntity reservationEntity = em.find(RoomReservationEntity.class, reservationId);

        if (reservationEntity != null) {
            return reservationEntity;
        } else {
            String errorMessage = " ID " + reservationId + " does not exist!";
            throw new ReservationNotFoundException(errorMessage);
        }
    }
    
    
    public RoomReservationEntity makeReservation(Long userId, String roomTypeName, double amount,
                                int paymentType, int numberOfRooms, LocalDate checkinDate, LocalDate checkoutDate) 
                                    throws RoomTypeNotFoundException, InvalidRoomReservationEntityException,
                                             LineItemExistException, UnknownPersistenceException{
    try {
            UserEntity user = em.find(UserEntity.class, userId);
            RoomReservationEntity newReservation = new RoomReservationEntity();
            BigDecimal totalAmount = BigDecimal.valueOf(amount);
            PaymentEntity newPayment = new PaymentEntity(totalAmount, PaymentMethodEnum.values()[paymentType - 1]);
            newReservation.setTotalAmount(totalAmount);
            newReservation.setPayment(newPayment);
            newReservation.setReservationDate(LocalDate.now());
            newReservation.setBookingAccount(user);

            
            RoomTypeEntity roomTypeEntity = roomTypeEntitySessionBeanLocal.retrieveRoomType(roomTypeName);

            for (int i = 0; i < numberOfRooms; i++) {
                RoomReservationLineItemEntity newLineItem = new RoomReservationLineItemEntity(roomTypeEntity, new BigDecimal(10000), checkinDate, checkoutDate);

                newReservation.getRoomReservationLineItems().add(newLineItem);

            }
            return this.createNewRoomReservationEntity(user.getUserId(), newReservation);
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    eJBContext.setRollbackOnly();
                    throw new LineItemExistException("Line item exist in the system");

                } else {
                    eJBContext.setRollbackOnly();
                    throw new UnknownPersistenceException("Unknown persist error");
                }
            } else {
                eJBContext.setRollbackOnly();
                throw new UnknownPersistenceException("Unknown persist error");
            }
        }

    }

    
}



