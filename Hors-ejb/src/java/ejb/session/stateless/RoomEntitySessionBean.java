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
        
        em.persist(newRoomEntity);
        em.flush();

        RoomTypeEntity roomTypeOfTheNewRoom = em.find(RoomTypeEntity.class, 
                newRoomEntity.getRoomType().getRoomTypeId());

        try {

            roomTypeOfTheNewRoom.getRoomEntities().add(newRoomEntity);

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
    
    // created for the purpose of data initialization
    @Override
    public void createNewRoom(RoomEntity newRoomEntity, List<RoomEntity> listOfRoomEntities) {
        listOfRoomEntities.add(newRoomEntity);
        em.persist(newRoomEntity);
    }
    


    @Override
    public RoomEntity retrieveRoomByRoomNumber(String roomNumber) throws RoomNotFoundException {
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

                roomEntityToUpdate.setRoomStatus(roomEntity.getRoomStatus());

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
    public Boolean checkIfTheRoomIsUsed(String roomNumber) {
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

    private List<RoomReservationLineItemEntity> findReservationUsingRoom(String roomNumber) {
        String databaseQuery = "SELECT s FROM RoomReservationLineItemEntity s"
                + " WHERE s.roomAllocation.roomNumber = :iRoomNumber ";
        Query query = em.createQuery(databaseQuery);
        query.setParameter("iRoomNumber", roomNumber);
        return query.getResultList();
    }

    @Override
    public void deleteRoom(String roomNumber) throws RoomNotFoundException {

        try {
            deleteUnusedRoom(roomNumber);
        } catch (RoomIsCurrentlyUsedException ex) {
            RoomEntity roomToBeDeleted = retrieveRoomByRoomNumber(roomNumber);
            roomToBeDeleted.setRoomStatus(RoomStatusEnum.NOTAVAILABLE);
        } catch (RoomNotFoundException ex) {
            throw new RoomNotFoundException(ex.getMessage());
        }
    }

    private void deleteUnusedRoom(String roomNumber) throws RoomIsCurrentlyUsedException,
            RoomNotFoundException {
        RoomEntity roomToBeDeleted;
        try {
            roomToBeDeleted = retrieveRoomByRoomNumber(roomNumber);
            if (roomToBeDeleted.getRoomStatus().equals(RoomStatusEnum.AVAILABLE)) {
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
