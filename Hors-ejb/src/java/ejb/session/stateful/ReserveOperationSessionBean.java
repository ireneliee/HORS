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
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.RoomStatusEnum;
import util.exception.GuestNotFoundException;
import util.exception.InvalidRoomReservationEntityException;
import util.exception.ReservationNotFoundException;
import util.exception.RoomTypeNotFoundException;
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
    
    private List<Pair> roomResults;
    private LocalDate checkinDate;
    private LocalDate checkoutDate; 
    private Integer numberOfRooms;

    public ReserveOperationSessionBean() {
        roomResults = new ArrayList<>();
    }

    public ReserveOperationSessionBean(List<Pair> roomResults, LocalDate checkinDate, LocalDate checkoutDate, Integer numberOfRooms) {
        this.roomResults = roomResults;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.numberOfRooms = numberOfRooms;
    }
    
    
    
    @PersistenceContext(unitName = "Hors-ejbPU")
    private EntityManager em;
    
    
    
    

    @Override
    public List<Pair> searchRoom(int reserveType, LocalDate checkinDate, LocalDate checkoutDate, Integer numberOfRooms) {
        
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.numberOfRooms = numberOfRooms;
     
        String queryString = "SELECT DISTINCT rt FROM RoomTypeEntity rt WHERE rt.disabled = false";
        Query query = em.createQuery(queryString);
        List<RoomTypeEntity> allRoomTypesAvailable = query.getResultList();
        
        for(RoomTypeEntity roomType : allRoomTypesAvailable) {
            Integer lowestRoomAvailibity = 1000;
            for(LocalDate date = checkinDate; date.isBefore(checkoutDate); date = date.plusDays(1)) {
                Integer roomAvailibility = calculateRoomAvailibility(roomType, date);
                lowestRoomAvailibity = Math.min(lowestRoomAvailibity, roomAvailibility);
            }
            if(lowestRoomAvailibity >= numberOfRooms) {
                BigDecimal totalFare = new BigDecimal(0);
                for(LocalDate date = checkinDate; date.isBefore(checkoutDate); date = date.plusDays(1)) {
                    if(reserveType == 1) {
                        totalFare = totalFare.add(computeFarePerNight(1, roomType, date));
                        System.out.println(totalFare);
                    }
                    else {
                        totalFare = totalFare.add(computeFarePerNight(4, roomType, date));
                    }
                }
                this.getRoomResults().add(new Pair(roomType, totalFare.multiply(new BigDecimal(numberOfRooms))));
                
                
            }
        }
        
         return this.getRoomResults();
     
    }
    
    private Integer calculateRoomAvailibility(RoomTypeEntity roomType, LocalDate date){
        
        String databaseQueryString = "SELECT rt FROM RoomEntity rt WHERE rt.roomType = :iRoomType AND rt.roomStatus = :iRoomStatus";
        Query query = em.createQuery(databaseQueryString);
        query.setParameter("iRoomType", roomType);
        query.setParameter("iRoomStatus", RoomStatusEnum.AVAILABLE);
        
        Integer totalRoomAvailableOfType = query.getResultList().size();
        
        databaseQueryString = "SELECT rrl FROM RoomReservationLineItemEntity rrl WHERE rrl.roomTypeEntity = :iRoomType AND rrl.checkInDate <= :iDate AND rrl.checkoutDate > :iDate";
        query = em.createQuery(databaseQueryString);
        query.setParameter("iRoomType", roomType);
        query.setParameter("iDate" , date);
        
        Integer totalRoomBooked = query.getResultList().size();
        
            
        
        return totalRoomAvailableOfType - totalRoomBooked;
    }
    
    
    private BigDecimal computeFarePerNight(Integer highestRank, RoomTypeEntity roomType, LocalDate date) {
        
        if(highestRank == 1) {
            String databaseQueryString = "SELECT rr FROM RoomRateEntity rr WHERE rr.roomRank = 1 AND rr.roomType.name = :iName AND rr.disabled = false";
            Query query = em.createQuery(databaseQueryString);
            query.setParameter("iName", roomType.getName());
            
            RoomRateEntity roomRate = (RoomRateEntity)query.getSingleResult();
            return roomRate.getRate();
            
        } else {
            String databaseQueryString = "SELECT rr FROM RoomRateEntity rr WHERE rr.roomRank = (SELECT max(r.roomRank) FROM RoomRateEntity r WHERE rr.roomType.name = :iName) "
                                            + "AND rr.roomType.name = :iName AND rr.startValidityDate <= :iDate AND rr.endValidityDate > :iDate";
            Query query = em.createQuery(databaseQueryString);
            query.setParameter("iName", roomType.getName());
            query.setParameter("iDate" , date);
            
           
            
            RoomRateEntity roomRate = (RoomRateEntity) query.getSingleResult();
           
            return roomRate.getRate();
        }
        
    }
    
    @Override
    public Long makeReservation(UserEntity username,int response, PaymentEntity payment) throws RoomTypeNotFoundException, InvalidRoomReservationEntityException{
        
        RoomReservationEntity newReservation = new RoomReservationEntity();
        BigDecimal price = this.getRoomResults().get(response).getPrice();
        newReservation.setTotalAmount(price);
        newReservation.setPayment(payment);
        newReservation.setReservationDate(LocalDate.now());
        newReservation.setBookingAccount(username);
        
        
        RoomTypeEntity roomType = this.getRoomResults().get(response - 1).getRoomType();
        RoomTypeEntity roomTypeEntity = roomTypeEntitySessionBeanLocal.retrieveRoomType(roomType.getName());
        
        
        for(int i = 0; i < numberOfRooms; i++) {
            RoomReservationLineItemEntity newLineItem = new RoomReservationLineItemEntity(roomTypeEntity, new BigDecimal(10000),checkinDate, checkoutDate);
            em.persist(newLineItem);
            em.flush();
            newReservation.getRoomReservationLineItems().add(newLineItem);
                      
        }
        return roomReservationEntitySessionBeanLocal.createNewRoomReservationEntity(newReservation);
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


