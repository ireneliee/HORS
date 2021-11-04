/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.NormalRateEntity;
import entity.PeakRateEntity;
import entity.PromotionRateEntity;
import entity.PublishedRateEntity;
import entity.RoomReservationLineItemEntity;
import entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.RoomStatusEnum;
import util.exception.PeakRateNotFoundException;
import util.exception.PromotionRateNotFoundException;
import util.exception.RateNotFoundException;

/**
 *
 * @author irene
 */
@Stateless
public class SearchSessionBean implements SearchSessionBeanRemote, SearchSessionBeanLocal {

    @PersistenceContext(unitName = "Hors-ejbPU")
    private EntityManager em;

    public SearchSessionBean() {
    }

    @Override
    public BigDecimal calculatePublishedRate(LocalDate checkIn, LocalDate checkOut,
            RoomTypeEntity roomType) throws RateNotFoundException {
        roomType = em.find(RoomTypeEntity.class, roomType.getRoomTypeId());
        String databaseQuery = "SELECT pr FROM PublishedRateEntity pr WHERE pr.roomType = :iRoomType";
        Query query = em.createQuery(databaseQuery);
        query.setParameter("iRoomType", roomType);
        try {
            PublishedRateEntity ratePerDay = (PublishedRateEntity) query.getSingleResult();
            BigDecimal rate = new BigDecimal("0");
            BigDecimal nominalRate = ratePerDay.getRate();

            for (LocalDate date = checkIn; date.isBefore(checkOut); date = date.plusDays(1)) {
                rate = rate.add(nominalRate);
            }

            return rate;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new RateNotFoundException("The published rate for this type of room does not exist.");
        }

    }

    // baseline -> get normal rate
    @Override
    public BigDecimal calculateNonPublishedRate(LocalDate checkIn, LocalDate checkOut,
            RoomTypeEntity roomType) throws RateNotFoundException {
        BigDecimal rate = new BigDecimal("0");
        for (LocalDate date = checkIn; date.isBefore(checkOut); date = date.plusDays(1)) {
            if (checkIfPromotionExist(roomType, date)) {
                try {
                    rate = rate.add(calculatePromotionRate(roomType, date));
                } catch (PromotionRateNotFoundException ex) {
                    System.out.println("There's a bug here leh!");
                }
            } else if (checkIfPeakRateExist(roomType, date)) {
                try {
                    rate = rate.add(calculatePeakRate(roomType, date));
                } catch (PeakRateNotFoundException ex) {
                    System.out.println("There's a bug here leh!");
                }
            } else {
                try {
                    rate = rate.add(calculateNormalRate(roomType));
                } catch (RateNotFoundException ex) {
                    throw new RateNotFoundException("There are no rate found for this room type.");
                }
            }
        }

        return rate;

    }

    private BigDecimal calculateNormalRate(RoomTypeEntity roomType)
            throws RateNotFoundException {
        roomType = em.find(RoomTypeEntity.class, roomType.getRoomTypeId());
        String databaseQuery = "SELECT pr FROM NormalRateEntity pr WHERE pr.roomType = :iRoomType";
        Query query = em.createQuery(databaseQuery);
        query.setParameter("iRoomType", roomType);
        try {
            NormalRateEntity ratePerDay = (NormalRateEntity) query.getSingleResult();

            BigDecimal nominalRate = ratePerDay.getRate();

            return nominalRate;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new RateNotFoundException("The normal rate for this type of room does not exist.");
        }

    }

    private BigDecimal calculatePromotionRate(RoomTypeEntity roomType, LocalDate date)
            throws PromotionRateNotFoundException {
        roomType = em.find(RoomTypeEntity.class, roomType.getRoomTypeId());
        String queryInString = "SELECT pr FROM PromotionRateEntity pr WHERE pr.roomType = :iRoomType";
        Query query = em.createQuery(queryInString);
        query.setParameter("iRoomType", roomType);
        List<PromotionRateEntity> listOfPromotionRates = query.getResultList();

        List<PromotionRateEntity> listOfOverlappingPromotionRates = listOfPromotionRates
                .stream()
                .filter(x -> dateIsOverlapping(date, x.getStartValidityDate(), x.getEndValidityDate()))
                .collect(Collectors.toList());

        if (listOfOverlappingPromotionRates.isEmpty()) {
            throw new PromotionRateNotFoundException("There are no promotion rate available!");
        }

        BigDecimal rate = new BigDecimal("0");

        for (int j = 0; j < listOfOverlappingPromotionRates.size(); j++) {
            BigDecimal currentPromotionRate = listOfOverlappingPromotionRates.get(j).getRate();
            rate = rate.min(currentPromotionRate);
        }

        return rate;

    }

    // method to check if promotion exists for a certain type of room, for a certain date
    private Boolean checkIfPromotionExist(RoomTypeEntity roomType, LocalDate date) {
        roomType = em.find(RoomTypeEntity.class, roomType.getRoomTypeId());
        String queryInString = "SELECT pr FROM PromotionRateEntity pr WHERE pr.roomType = :iRoomType";
        Query query = em.createQuery(queryInString);
        query.setParameter("iRoomType", roomType);
        List<PromotionRateEntity> listOfPromotionRates = query.getResultList();

        return listOfPromotionRates
                .stream()
                .filter(x -> dateIsOverlapping(date, x.getStartValidityDate(), x.getEndValidityDate()))
                .findAny()
                .isPresent();

    }

    private BigDecimal calculatePeakRate(RoomTypeEntity roomType, LocalDate date)
            throws PeakRateNotFoundException {
        roomType = em.find(RoomTypeEntity.class, roomType.getRoomTypeId());
        String queryInString = "SELECT pr FROM PeakRateEntity pr WHERE pr.roomType = :iRoomType";
        Query query = em.createQuery(queryInString);
        query.setParameter("iRoomType", roomType);
        List<PeakRateEntity> listOfPeakRates = query.getResultList();

        List<PeakRateEntity> listOfOverlappingPeakRates = listOfPeakRates
                .stream()
                .filter(x -> dateIsOverlapping(date, x.getStartValidityDate(), x.getEndValidityDate()))
                .collect(Collectors.toList());

        if (listOfOverlappingPeakRates.isEmpty()) {
            throw new PeakRateNotFoundException("There are no promotion rate available!");
        }

        BigDecimal rate = new BigDecimal("0");

        for (int j = 0; j < listOfOverlappingPeakRates.size(); j++) {
            BigDecimal currentPromotionRate = listOfOverlappingPeakRates.get(j).getRate();
            rate = rate.min(currentPromotionRate);
        }

        return rate;

    }

    private Boolean checkIfPeakRateExist(RoomTypeEntity roomType, LocalDate date) {
        roomType = em.find(RoomTypeEntity.class, roomType.getRoomTypeId());
        String queryInString = "SELECT pr FROM PeakRateEntity pr WHERE pr.roomType = :iRoomType";
        Query query = em.createQuery(queryInString);
        query.setParameter("iRoomType", roomType);
        List<PeakRateEntity> listOfPeakRates = query.getResultList();

        return listOfPeakRates
                .stream()
                .filter(x -> dateIsOverlapping(date, x.getStartValidityDate(), x.getEndValidityDate()))
                .findAny()
                .isPresent();

    }

    private Boolean dateIsOverlapping(LocalDate dateToEnquire, LocalDate startRange,
            LocalDate endRange) {
        return (dateToEnquire.isEqual(startRange))
                || (dateToEnquire.isEqual(endRange))
                || ((dateToEnquire.isAfter(startRange)) && dateToEnquire.isBefore(endRange));
    }

    @Override
    public Map<RoomTypeEntity, Integer> findAvailableRoomTypes(LocalDate checkIn, LocalDate checkOut) {
        Map<RoomTypeEntity, Integer> availableRooms = new HashMap<>();

        // retrieving list of distinct room type entities which are available
        String queryString = "SELECT DISTINCT s FROM RoomTypeEntity s WHERE s.disabled = false";
        Query query = em.createQuery(queryString);
        List<RoomTypeEntity> listOfAvailableRoomType = query.getResultList();

        // copy the list to the hashmap
        listOfAvailableRoomType
                .stream()
                .forEach(x -> availableRooms.put(x, 1000));

        for (int i = 0; i < listOfAvailableRoomType.size(); i++) {
            for (LocalDate date = checkIn; date.isBefore(checkOut); date = date.plusDays(1)) {
                RoomTypeEntity currentRoomType = listOfAvailableRoomType.get(i);
                Integer currentNumberOfAvailableRoom = availableRooms.get(currentRoomType);
                Integer availableRoomThatDate
                        = findNumberOfRoomsOfCertainRoomTypeAvailableOnCertainDate(currentRoomType, date);
                Integer minNumberOfAvailableRoom = Math.min(currentNumberOfAvailableRoom,
                        availableRoomThatDate);
                availableRooms.replace(currentRoomType, minNumberOfAvailableRoom);
            }
        }

        return availableRooms;
    }

    private Integer findNumberOfRoomsOfCertainRoomTypeAvailableOnCertainDate(RoomTypeEntity roomType,
            LocalDate dateToday) {
        roomType = em.find(RoomTypeEntity.class, roomType.getRoomTypeId());
        // finding number of total rooms of the certain room type
        String queryString = "SELECT s FROM RoomEntity s WHERE s.roomType =:iRoomType"
                + " AND s.roomStatus = :iRoomStatus";
        Query databaseQuery = em.createQuery(queryString);
        databaseQuery.setParameter("iRoomType", roomType);
        databaseQuery.setParameter("iRoomStatus", RoomStatusEnum.AVAILABLE);

        Integer numberOfAvailableRoomsBeforeReservations = databaseQuery.getResultList().size();
        Integer numberOfReservationBlockingTheDate
                = findNumberOfReservationsUsingThatRoomTypeBlockingTheDate(roomType, dateToday);

        return numberOfAvailableRoomsBeforeReservations - numberOfReservationBlockingTheDate;
    }

    private Integer findNumberOfReservationsUsingThatRoomTypeBlockingTheDate(RoomTypeEntity roomType,
            LocalDate dateToday) {
        roomType = em.find(RoomTypeEntity.class, roomType.getRoomTypeId());
        String queryString = "SELECT s FROM RoomReservationLineItemEntity s WHERE "
                + "s.roomTypeEntity = :iRoomType";
        Query databaseQuery = em.createQuery(queryString);
        databaseQuery.setParameter("iRoomType", roomType);

        //retrieve the list of room reservations using the room type
        List<RoomReservationLineItemEntity> listOfReservations = databaseQuery.getResultList();

        // filter to find only room reservations that are using that room type on the date
        Long numberOfReservationUsingOnThatDate = listOfReservations
                .stream()
                .filter(x -> reservationIsUsingTheRoomOnThatDate(x, dateToday))
                .count();

        return numberOfReservationUsingOnThatDate.intValue();

    }

    private Boolean reservationIsUsingTheRoomOnThatDate(RoomReservationLineItemEntity roomReservation,
            LocalDate usingDate) {
        roomReservation = em.find(RoomReservationLineItemEntity.class, roomReservation.getRoomReservationLineItemId());
        LocalDate checkInDateOfReservation = roomReservation.getCheckInDate();
        LocalDate checkOutDateOfReservation = roomReservation.getCheckoutDate();
        return (checkInDateOfReservation.isBefore(usingDate) || checkInDateOfReservation.isEqual(usingDate))
                && checkOutDateOfReservation.isAfter(usingDate);
    }

}
