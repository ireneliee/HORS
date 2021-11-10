/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import ejb.session.stateless.RoomEntitySessionBeanLocal;
import ejb.session.stateless.RoomReservationEntitySessionBeanLocal;
import ejb.session.stateless.RoomTypeEntitySessionBeanLocal;
import entity.PaymentEntity;
import entity.RoomRateEntity;
import entity.RoomReservationEntity;
import entity.RoomReservationLineItemEntity;
import entity.RoomTypeEntity;
import entity.UserEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.enumeration.RoomStatusEnum;
import util.exception.GuestNotFoundException;
import util.exception.InvalidRoomReservationEntityException;
import util.exception.LineItemExistException;
import util.exception.NoAvailableRoomOptionException;
import util.exception.ReservationNotFoundException;
import util.exception.RoomTypeNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;
import util.reservation.Pair;

/**
 *
 * @author zenyew
 */
@Stateful
public class ReserveOperationSessionBean implements ReserveOperationSessionBeanRemote, ReserveOperationSessionBeanLocal {

    @EJB
    private RoomReservationEntitySessionBeanLocal roomReservationEntitySessionBeanLocal;

    @EJB
    private RoomTypeEntitySessionBeanLocal roomTypeEntitySessionBeanLocal;

    @EJB(name = "RoomEntitySessionBeanLocal")
    private RoomEntitySessionBeanLocal roomEntitySessionBeanLocal;

    @Resource
    private EJBContext eJBContext;

    private List<Pair> roomResults;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private Integer numberOfRooms;

    public ReserveOperationSessionBean() {

    }

    public ReserveOperationSessionBean(List<Pair> roomResults, LocalDate checkinDate, LocalDate checkoutDate, Integer numberOfRooms) {

        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.numberOfRooms = numberOfRooms;
    }

    @PersistenceContext(unitName = "Hors-ejbPU")
    private EntityManager em;

    @Override
    public List<Pair> searchRoom(int reserveType, LocalDate checkinDate, LocalDate checkoutDate, Integer numberOfRooms) throws NoAvailableRoomOptionException {

        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.numberOfRooms = numberOfRooms;
        this.roomResults = new ArrayList<>();

        try {
            String queryString = "SELECT rt FROM RoomTypeEntity rt WHERE rt.disabled = false";
            Query query = em.createQuery(queryString);
            List<RoomTypeEntity> allRoomTypesAvailable = query.getResultList();
            //debugging
            System.out.println("Numebr of room type is: " + allRoomTypesAvailable.size());

            for (RoomTypeEntity roomType : allRoomTypesAvailable) {
                Integer lowestRoomAvailibity = 1000;
                for (LocalDate date = checkinDate; date.isBefore(checkoutDate); date = date.plusDays(1)) {
                    Integer roomAvailability = calculateRoomAvailibility(roomType, date);
                    lowestRoomAvailibity = Math.min(lowestRoomAvailibity, roomAvailability);
                }
                System.out.println("Room type: " + roomType.getName() + lowestRoomAvailibity);

                if (lowestRoomAvailibity >= numberOfRooms && checkIfRoomRateExist(roomType)) {
                    System.out.println(roomType.getName() + " pass the test.");
                    BigDecimal totalFare = new BigDecimal(0);
                    for (LocalDate date = checkinDate; date.isBefore(checkoutDate); date = date.plusDays(1)) {
                        if (reserveType == 1) {
                            totalFare = totalFare.add(computeFarePerNight(1, roomType, date));
                            System.out.println(totalFare);
                        } else {
                            totalFare = totalFare.add(computeFarePerNight(4, roomType, date));
                        }
                    }
                    this.getRoomResults().add(new Pair(roomType, totalFare.multiply(new BigDecimal(numberOfRooms))));

                }
            }

            return this.getRoomResults();
        } catch (NoResultException | NonUniqueResultException | ArrayIndexOutOfBoundsException ex) {

            throw new NoAvailableRoomOptionException("No available room that matches the input");
        }
    }
    
    private boolean checkIfRoomRateExist(RoomTypeEntity roomType) {
        try {
            String queryString2 = "SELECT rt FROM RoomRateEntity rt WHERE rt.roomType = :iRoomType";
            Query query2 = em.createQuery(queryString2);
            query2.setParameter("iRoomType", roomType);
            List<RoomRateEntity> listOfRoomRates = query2.getResultList();
            return  true;
        } catch (NoResultException ex) {
            return false;
        }
    }

    private Integer calculateRoomAvailibility(RoomTypeEntity roomType, LocalDate date) throws NoResultException, NonUniqueResultException {

        try {
            String databaseQueryString = "SELECT rt FROM RoomEntity rt WHERE rt.roomType = :iRoomType AND rt.roomStatus = :iRoomStatus";
            Query query = em.createQuery(databaseQueryString);
            query.setParameter("iRoomType", roomType);
            query.setParameter("iRoomStatus", RoomStatusEnum.AVAILABLE);

            Integer totalRoomAvailableOfType = query.getResultList().size();

            databaseQueryString = "SELECT rrl FROM RoomReservationLineItemEntity rrl WHERE rrl.roomTypeEntity = :iRoomType AND rrl.checkInDate <= :iDate AND rrl.checkoutDate > :iDate";
            query = em.createQuery(databaseQueryString);
            query.setParameter("iRoomType", roomType);
            query.setParameter("iDate", date);

            Integer totalRoomBooked = query.getResultList().size();

            return totalRoomAvailableOfType - totalRoomBooked;
        } catch (NoResultException | NonUniqueResultException | ArrayIndexOutOfBoundsException ex) {

            throw ex;
        }
    }

    private BigDecimal computeFarePerNight(Integer highestRank, RoomTypeEntity roomType, LocalDate date) throws NoResultException, NonUniqueResultException, ArrayIndexOutOfBoundsException {
        try {
            if (highestRank == 1) {
                String databaseQueryString = "SELECT rr FROM RoomRateEntity rr WHERE rr.roomRank = 1 AND rr.roomType.name = :iName AND rr.disabled = false ORDER BY rr.rate ASC";
                Query query = em.createQuery(databaseQueryString);
                query.setParameter("iName", roomType.getName());

                RoomRateEntity roomRate = (RoomRateEntity) query.getResultList().get(0);
                return roomRate.getRate();

            } else {
                String databaseQueryString = "SELECT rr FROM RoomRateEntity rr WHERE rr.roomRank = (SELECT max(r.roomRank) FROM RoomRateEntity r "
                        + "WHERE r.startValidityDate <= :iDate AND r.endValidityDate > :iDate AND  r.roomType.name = :iName) "
                        + "AND rr.roomType.name = :iName AND rr.startValidityDate <= :iDate AND rr.endValidityDate > :iDate ORDER BY rr.rate ASC";
                Query query = em.createQuery(databaseQueryString);
                query.setParameter("iName", roomType.getName());
                query.setParameter("iDate", date);

                RoomRateEntity roomRate = (RoomRateEntity) query.getResultList().get(0);

                return roomRate.getRate();
            }
        } catch (NoResultException | NonUniqueResultException | ArrayIndexOutOfBoundsException ex) {

            throw ex;
        }

    }

    @Override
    public Long makeReservation(UserEntity user, List<Pair> roomResults, int response, PaymentEntity payment) throws RoomTypeNotFoundException, InvalidRoomReservationEntityException,
            LineItemExistException, UnknownPersistenceException {

        try {
            user = em.find(UserEntity.class, user.getUserId());
            RoomReservationEntity newReservation = new RoomReservationEntity();
            BigDecimal price = this.getRoomResults().get(response).getPrice();
            newReservation.setTotalAmount(price);
            newReservation.setPayment(payment);
            newReservation.setReservationDate(LocalDate.now());
            newReservation.setBookingAccount(user);

            RoomTypeEntity roomType = this.getRoomResults().get(response).getRoomType();
            RoomTypeEntity roomTypeEntity = roomTypeEntitySessionBeanLocal.retrieveRoomType(roomType.getName());

            for (int i = 0; i < numberOfRooms; i++) {
                RoomReservationLineItemEntity newLineItem = new RoomReservationLineItemEntity(roomTypeEntity, new BigDecimal(10000), checkinDate, checkoutDate);

                newReservation.getRoomReservationLineItems().add(newLineItem);

            }
            return roomReservationEntitySessionBeanLocal.createNewRoomReservationEntity(user.getUserId(), newReservation);
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

    /**
     * @return the roomResults
     */
    private List<Pair> getRoomResults() {
        return roomResults;
    }

    /**
     * @param roomResults the roomResults to set
     */
    private void setRoomResults(List<Pair> roomResults) {
        this.roomResults = roomResults;
    }

    /**
     * @return the checkinDate
     */
    private LocalDate getCheckinDate() {
        return checkinDate;
    }

    /**
     * @param checkinDate the checkinDate to set
     */
    private void setCheckinDate(LocalDate checkinDate) {
        this.checkinDate = checkinDate;
    }

    /**
     * @return the checkoutDate
     */
    private LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    /**
     * @param checkoutDate the checkoutDate to set
     */
    private void setCheckoutDate(LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    /**
     * @return the numberOfRooms
     */
    private Integer getNumberOfRooms() {
        return numberOfRooms;
    }

    /**
     * @param numberOfRooms the numberOfRooms to set
     */
    private void setNumberOfRooms(Integer numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    private void persist(Object object) {
        em.persist(object);
    }
}
