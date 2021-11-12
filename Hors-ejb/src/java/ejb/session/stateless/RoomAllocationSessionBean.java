/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomAllocationExceptionEntity;
import entity.RoomEntity;
import entity.RoomReservationEntity;
import entity.RoomReservationLineItemEntity;
import entity.RoomTypeEntity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.RoomStatusEnum;
import util.exception.NoMoreRoomToAccomodateException;
import util.exception.RoomAllocationExceptionReportDoesNotExistException;
import util.exception.RoomAllocationIsDoneException;

/**
 *
 * @author irene
 */
@Stateless
public class RoomAllocationSessionBean implements RoomAllocationSessionBeanRemote, RoomAllocationSessionBeanLocal {

    @PersistenceContext(unitName = "Hors-ejbPU")
    private EntityManager em;

    public RoomAllocationSessionBean() {
    }

    @Override
    public RoomAllocationExceptionEntity retrieveReportByDate(LocalDate reportDate) throws RoomAllocationExceptionReportDoesNotExistException {
        Query query = em.createQuery("SELECT r FROM RoomAllocationExceptionEntity r WHERE "
                + "r.dateOfAllocation = :iDate");
        query.setParameter("iDate", reportDate);
        try {
            return (RoomAllocationExceptionEntity) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new RoomAllocationExceptionReportDoesNotExistException("Room allocation exception does not exist. "
                    + "Room allocation has not been done.");
        }
    }

    @Schedule(dayOfWeek = "*", hour = "2")
    private void dailyAllocationOfRoom() {
        LocalDate dateOfToday = LocalDate.now();
        try {
            allocateRoomGivenDate(dateOfToday);
        } catch (RoomAllocationIsDoneException ex) {
            System.out.print("Error occured: " + ex.getMessage());
        }
    }

    // to immediately allocate for booking after 2am
    public void allocateRoomNow(RoomReservationEntity roomReservation) {
        List<RoomReservationLineItemEntity> toBeAllocated = roomReservation.getRoomReservationLineItems();
        RoomReservationLineItemEntity firstReservation = toBeAllocated.get(0);
        LocalDate checkinDate = firstReservation.getCheckInDate();
        RoomTypeEntity typeOfRoom = firstReservation.getRoomTypeEntity();

        mainAllocationAlgorithm(toBeAllocated, checkinDate, typeOfRoom);

    }

    private void mainAllocationAlgorithm(List<RoomReservationLineItemEntity> toBeAllocated, LocalDate checkinDate,
            RoomTypeEntity typeOfRoom) {
        RoomAllocationExceptionEntity exceptionReport;
        try {
            exceptionReport = retrieveReportByDate(checkinDate);
        } catch (RoomAllocationExceptionReportDoesNotExistException ex) {
            exceptionReport = new RoomAllocationExceptionEntity(checkinDate);
        }

        List<RoomEntity> availableRooms = retrieveListOfAvailableRoom(typeOfRoom, checkinDate);

        for (int i = 0; i < toBeAllocated.size(); i++) {

            // if there is still enough room
            //System.out.println("Reach Y");
            if (i >= availableRooms.size()) {

                RoomReservationLineItemEntity roomReservationToAllocate = toBeAllocated.get(i);
                roomReservationToAllocate = em.find(RoomReservationLineItemEntity.class,
                        roomReservationToAllocate.getRoomReservationLineItemId());

                try {
                    handlingTypeOneException(roomReservationToAllocate, typeOfRoom);
                    exceptionReport.getTypeOneException().add(roomReservationToAllocate);
                    System.out.println("Type 1 exception: RoomReservationLineItemEntity allocated" + roomReservationToAllocate);
                } catch (NoMoreRoomToAccomodateException ex) {
                    exceptionReport.getTypeTwoException().add(roomReservationToAllocate);
                    System.out.println("Type 2 exception: RoomReservationLineItemEntity allocated" + roomReservationToAllocate);
                }
            } else {
                // allocating room
                RoomEntity roomToBeAllocated = availableRooms.get(i);
                RoomReservationLineItemEntity roomReservationToAllocate = toBeAllocated.get(i);
                roomToBeAllocated = em.find(RoomEntity.class, roomToBeAllocated.getRoomEntityId());
                roomReservationToAllocate = em.find(RoomReservationLineItemEntity.class,
                        roomReservationToAllocate.getRoomReservationLineItemId());
                roomReservationToAllocate.setRoomAllocation(roomToBeAllocated);
                System.out.println("***Room allocation is successful***");
                System.out.println("Room to be allocated" + roomToBeAllocated.getRoomType());
                System.out.println("RoomReservationLineItemEntity allocated" + roomReservationToAllocate);
                em.merge(roomReservationToAllocate);

                // remove room that has been allocated from the list of available rooms
                //availableRooms.remove(roomToBeAllocated);
            }
        }
        em.persist(exceptionReport);
        em.flush();
    }

    @Override
    public void allocateRoomGivenDate(LocalDate checkInDate) throws RoomAllocationIsDoneException {
        // Check if rooms have been allocated that day
        if (roomAllocationIsDone(checkInDate)) {
            throw new RoomAllocationIsDoneException("Room allocation for " + checkInDate.toString()
                    + " has already been done.");
        }

        // retrieve list of all room types. Allocation will be done one by one -> following the room type
        String databaseQueryInString = "SELECT DISTINCT rt FROM RoomTypeEntity rt  ORDER BY rt.roomRanking ASC";
        Query query = em.createQuery(databaseQueryInString);
        List<RoomTypeEntity> listOfRoomTypes = query.getResultList();

        listOfRoomTypes
                .stream()
                .forEach(x -> allocateRoomToday(checkInDate, x));

    }

    // allocate room reservation to their room by roomType
    private void allocateRoomToday(LocalDate checkInDate, RoomTypeEntity typeOfRoom) {

        List<RoomEntity> availableRooms = retrieveListOfAvailableRoom(typeOfRoom, checkInDate);
        System.out.println("***Retrieving available room... for " + typeOfRoom.getName() + "***");

        //retrieve list of reservation which request for that room type y, check in date x
        String reservationQuery = " SELECT rr FROM RoomReservationLineItemEntity rr WHERE "
                + "rr.roomTypeEntity = :iRoomType AND rr.checkInDate = :iCheckInDate";
        Query reservationDatabaseQuery = em.createQuery(reservationQuery);
        reservationDatabaseQuery.setParameter("iRoomType", typeOfRoom);
        reservationDatabaseQuery.setParameter("iCheckInDate", checkInDate);
        System.out.println("***Retrieving all room reservations requiring the room type" + typeOfRoom.getName() + "***");

        List<RoomReservationLineItemEntity> toBeAllocated = reservationDatabaseQuery.getResultList();
        
        mainAllocationAlgorithm(toBeAllocated,checkInDate, typeOfRoom);

    }

    private void handlingTypeOneException(RoomReservationLineItemEntity roomReservation,
            RoomTypeEntity roomType) throws NoMoreRoomToAccomodateException {
        //System.out.println("Reach E");
        Integer nextRoomRank = roomType.getRoomRanking() + 1;

        if (nextRoomRank == 6) {
            throw new NoMoreRoomToAccomodateException("There are no more room available. ");
        }

        String databaseQueryInString = "SELECT rt FROM RoomTypeEntity rt WHERE "
                + "rt.roomRanking = :iRoomRanking";
        Query query = em.createQuery(databaseQueryInString);
        query.setParameter("iRoomRanking", nextRoomRank);

        List<RoomTypeEntity> listOfRoomTypeOfHigherRank = query.getResultList();

        RoomTypeEntity higherRoomType = listOfRoomTypeOfHigherRank.get(0);
        LocalDate checkinDate = roomReservation.getCheckInDate();

        List<RoomEntity> listOfAvailableRoom = retrieveListOfAvailableRoom(higherRoomType, checkinDate);
        System.out.println("No. of available room " + listOfAvailableRoom.size());

        if (!listOfAvailableRoom.isEmpty()) {
            RoomEntity roomAssigned = listOfAvailableRoom.get(0);
            roomReservation.setRoomAllocation(roomAssigned);
            System.out.println("***Room allocation is successful***");
            System.out.println("Room to be allocated" + roomAssigned.getRoomType());
            System.out.println("Reservation " + roomReservation);
            System.out.println("Type of room requested" + roomReservation.getRoomTypeEntity());
        } else {
            throw new NoMoreRoomToAccomodateException("There are no more room available. ");
        }
        //System.out.println("Reach F");
    }

    private Boolean roomAllocationIsDone(LocalDate checkInDate) {
        String databaseQueryInString = "SELECT rr FROM RoomReservationLineItemEntity rr "
                + "WHERE rr.checkInDate = :iCheckInDate";
        Query databaseQuery = em.createQuery(databaseQueryInString);
        databaseQuery.setParameter("iCheckInDate", checkInDate);
        List<RoomReservationLineItemEntity> listOfRoomReservations = databaseQuery.getResultList();
        for (RoomReservationLineItemEntity roomReservation : listOfRoomReservations) {
            if (!(roomReservation.getRoomAllocation() == null)) {
                return true;
            }
        }
        //System.out.println("Reach G");
        return false;

    }

    // before minusing other reservations blocking it
    private List<RoomEntity> retrieveListOfAvailableRoomsByType(RoomTypeEntity roomType) {
        //System.out.println("Reach L");
        String databaseQueryInString = "SELECT r FROM RoomEntity r WHERE r.roomType = :iRoomType "
                + "AND r.roomStatus = :iRoomStatus";
        Query databaseQuery = em.createQuery(databaseQueryInString);
        databaseQuery.setParameter("iRoomType", roomType);
        databaseQuery.setParameter("iRoomStatus", RoomStatusEnum.AVAILABLE);

        return databaseQuery.getResultList();
    }

    // retrieve available room that can be used for today's room allocation
    // check if the checkIndate is before, and checkout date is after
    private List<RoomEntity> retrieveListOfAvailableRoom(RoomTypeEntity roomType,
            LocalDate checkInDate) {
        List<RoomEntity> listOfAvailableRoomThatDay = new ArrayList<>();
        List<RoomEntity> listOfAvailableRoom = retrieveListOfAvailableRoomsByType(roomType);

        for (RoomEntity room : listOfAvailableRoom) {

            // finding all the  reservations that are using a certain room
            String databaseQueryInString = "SELECT r FROM RoomReservationLineItemEntity r WHERE r.roomAllocation = :iRoomEntity";
            Query databaseQuery = em.createQuery(databaseQueryInString);
            databaseQuery.setParameter("iRoomEntity", room);

            //checking if there is any reservation using that room on the check in day
            List<RoomReservationLineItemEntity> listOfReservationUsingThatRoom = databaseQuery.getResultList();
            Boolean theRoomIsNotAvailable = listOfReservationUsingThatRoom
                    .stream()
                    .filter(x -> reservationIsUsingTheRoomOnThatDate(x,
                    checkInDate))
                    .findAny()
                    .isPresent();

            if (!theRoomIsNotAvailable) {
                listOfAvailableRoomThatDay.add(room);
            }

        }
        return listOfAvailableRoomThatDay;
    }

    private Boolean reservationIsUsingTheRoomOnThatDate(RoomReservationLineItemEntity roomReservation,
            LocalDate checkInDate) {

        LocalDate checkInDateOfReservation = roomReservation.getCheckInDate();
        LocalDate checkOutDateOfReservation = roomReservation.getCheckoutDate();
        return (checkInDateOfReservation.isBefore(checkInDate) || checkInDateOfReservation.isEqual(checkInDate))
                && checkOutDateOfReservation.isAfter(checkInDate);
    }

}
