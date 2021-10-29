/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomEntity;
import entity.RoomReservationLineItemEntity;
import entity.RoomTypeEntity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import util.enumeration.RoomStatusEnum;
import util.exception.InputDataValidationException;
import util.exception.RoomIsCurrentlyUsedException;
import util.exception.RoomNotFoundException;
import util.exception.RoomNumberExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateRoomException;

@Stateless
public class RoomEntitySessionBean implements RoomEntitySessionBeanRemote, RoomEntitySessionBeanLocal {

    @PersistenceContext(unitName = "Hors-ejbPU")
    private EntityManager em;
    //private final ValidatorFactory validatorFactory;
    //private final Validator validator;

    public RoomEntitySessionBean() {
        //validatorFactory = Validation.buildDefaultValidatorFactory();
        //validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewRoom(RoomEntity newRoomEntity) throws RoomNumberExistException,
            UnknownPersistenceException, InputDataValidationException {
        
       RoomTypeEntity roomTypeOfTheNewRoom = em.find(RoomTypeEntity.class, newRoomEntity.getRoomType()
       .getRoomTypeId());
       /*
       roomTypeOfTheNewRoom.getRoomEntities().size();
       roomTypeOfTheNewRoom.getRoomTypeAvailabilities().size();
       */
        
        try {

            roomTypeOfTheNewRoom.getRoomEntities().add(newRoomEntity);
            /*
            roomTypeOfTheNewRoom.getRoomTypeAvailabilities()
                    .stream()
                    .forEach(x -> x.incrementNoOfAvailableRoomByOne());
            */

            em.persist(newRoomEntity);
            em.flush();

            return newRoomEntity.getRoomEntityId();
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new RoomNumberExistException("Room number existed! ");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    @Override
    public RoomEntity retrieveRoomByRoomNumber(Integer roomNumber) throws RoomNotFoundException {
        try {
            String databaseQuery = "SELECT s FROM RoomEntity s WHERE s.roomNumber =:iRoomNumber";
            Query query = em.createQuery(databaseQuery);
            query.setParameter("iRoomNumber", roomNumber);

            return (RoomEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new RoomNotFoundException("Room with room number " + roomNumber + " does not exist!");
        }
    }

    @Override
    public void updateRoom(RoomEntity roomEntity) throws RoomNotFoundException,
            UpdateRoomException, InputDataValidationException {
        /*
        if(roomEntity != null && roomEntity.getRoomEntityId() != null)
        {
            Set<ConstraintViolation<RoomEntity>>constraintViolations = validator.validate(roomEntity);
        
            if(constraintViolations.isEmpty())
            {
         */ try {
            RoomEntity roomEntityToUpdate = retrieveRoomByRoomNumber(roomEntity.getRoomNumber());

            if (roomEntityToUpdate.getRoomNumber().equals(roomEntity.getRoomNumber())) {
                RoomStatusEnum currentStatus = roomEntityToUpdate.getRoomStatus();
                RoomStatusEnum statusToBeChanged = roomEntity.getRoomStatus();

                roomEntityToUpdate.setRoomStatus(roomEntity.getRoomStatus());

                if (currentStatus.equals(RoomStatusEnum.AVAILABLE)
                        && statusToBeChanged.equals(RoomStatusEnum.NOTAVAILABLE)) {
                    roomEntityToUpdate
                            .getRoomType()
                            .getRoomTypeAvailabilities()
                            .forEach(x -> x.decreaseNoOfAvailableRoomByOne());
                }

                if (currentStatus.equals(RoomStatusEnum.NOTAVAILABLE)
                        && statusToBeChanged.equals(RoomStatusEnum.AVAILABLE)) {
                    roomEntityToUpdate
                            .getRoomType()
                            .getRoomTypeAvailabilities()
                            .forEach(x -> x.incrementNoOfAvailableRoomByOne());
                }

            } else {
                throw new UpdateRoomException("Room record to be updated does not match the existing record");
            }
        } catch (RoomNotFoundException ex) {
            throw new RoomNotFoundException("Room ID not provided for staff to be updated");
        }
    }

    @Override
    public List<RoomEntity> retrieveAllRooms() {
        String queryDatabase = "SELECT s FROM RoomEntity s";
        Query query = em.createQuery(queryDatabase);
        return query.getResultList();
    }

    @Override
    public Boolean checkIfTheRoomIsUsed(Integer roomNumber) {
        LocalDate now = LocalDate.now();
        String databaseQuery = "SELECT s FROM RoomReservationLineItemEntity s"
                + " WHERE s.roomAllocation.roomNumber = :iRoomNumber ";
        Query query = em.createQuery(databaseQuery);
        query.setParameter("iRoomNumber", roomNumber);
        List<RoomReservationLineItemEntity> listOfReservationsUsingThatRoom = query.getResultList();

        // checking if the checkout date of each roomReservationLineItemEntity is before today
        List<RoomReservationLineItemEntity> listOfReservationUsingThatRoomAfterToday
                = listOfReservationsUsingThatRoom
                        .stream()
                        .filter(x -> x.getCheckoutDate().isAfter(now))
                        .collect(Collectors.toCollection(ArrayList::new));

        return !listOfReservationUsingThatRoomAfterToday.isEmpty();

    }

    private List<RoomReservationLineItemEntity> findReservationUsingRoom(Integer roomNumber) {
        String databaseQuery = "SELECT s FROM RoomReservationLineItemEntity s"
                + " WHERE s.roomAllocation.roomNumber = :iRoomNumber ";
        Query query = em.createQuery(databaseQuery);
        query.setParameter("iRoomNumber", roomNumber);
        return query.getResultList();
    }

    @Override
    public void deleteRoom(Integer roomNumber) throws RoomNotFoundException {

        try {
            deleteUnusedRoom(roomNumber);
        } catch (RoomIsCurrentlyUsedException ex) {
            RoomEntity roomToBeDeleted = retrieveRoomByRoomNumber(roomNumber);
            roomToBeDeleted.setRoomStatus(RoomStatusEnum.NOTAVAILABLE);
            roomToBeDeleted
                    .getRoomType()
                    .getRoomTypeAvailabilities()
                    .forEach(x -> x.decreaseNoOfAvailableRoomByOne());
        } catch (RoomNotFoundException ex) {
            throw new RoomNotFoundException(ex.getMessage());
        }
    }

    private void deleteUnusedRoom(Integer roomNumber) throws RoomIsCurrentlyUsedException,
            RoomNotFoundException {
        RoomEntity roomToBeDeleted;
        try {
            roomToBeDeleted = retrieveRoomByRoomNumber(roomNumber);
            if (roomToBeDeleted.getRoomStatus().equals(RoomStatusEnum.AVAILABLE)) {
                roomToBeDeleted
                        .getRoomType()
                        .getRoomTypeAvailabilities()
                        .forEach(x -> x.decreaseNoOfAvailableRoomByOne());
            }
        } catch (RoomNotFoundException ex) {
            throw new RoomNotFoundException(ex.getMessage());
        }
        if (checkIfTheRoomIsUsed(roomNumber)) {
            throw new RoomIsCurrentlyUsedException("Room is currently allocated to a reservation. "
                    + " Hence, it can't be deleted.");
        }

        roomToBeDeleted.getRoomType().getRoomEntities().remove(roomToBeDeleted);

        List<RoomReservationLineItemEntity> listOfReservationsUsingThatRoom
                = findReservationUsingRoom(roomNumber);

        listOfReservationsUsingThatRoom
                .stream()
                .forEach(x -> x.setRoomAllocation(null));
        em.remove(roomToBeDeleted);
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<RoomEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; "
                    + constraintViolation.getMessage();
        }

        return msg;
    }

}
