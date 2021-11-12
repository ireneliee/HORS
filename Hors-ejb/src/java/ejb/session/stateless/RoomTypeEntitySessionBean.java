/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomTypeEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.RoomTypeExistException;
import util.exception.RoomTypeNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author irene
 */
@Stateless
public class RoomTypeEntitySessionBean implements RoomTypeEntitySessionBeanRemote, RoomTypeEntitySessionBeanLocal {

    @PersistenceContext(unitName = "Hors-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public RoomTypeEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createRoomType(RoomTypeEntity newRoomType) throws RoomTypeExistException,
            UnknownPersistenceException {
        Set<ConstraintViolation<RoomTypeEntity>>constraintViolations = validator.validate(newRoomType);
        if(!constraintViolations.isEmpty()) {
            throw new UnknownPersistenceException("Wrong data input.");
        }

        try {

            newRoomType.setDisabled(false);
            em.persist(newRoomType);
            em.flush();
            return newRoomType.getRoomTypeId();

        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException("Unknown persistence has occured.");

    }
    }

    @Override
    public List<RoomTypeEntity> retrieveAllRoomType() {
        String databaseQueryString = "SELECT s FROM RoomTypeEntity s";
        Query databaseQuery = em.createQuery(databaseQueryString);
        return databaseQuery.getResultList();

    }

    @Override
    public RoomTypeEntity retrieveRoomType(String name) throws RoomTypeNotFoundException {
        String databaseQueryString = "SELECT s FROM RoomTypeEntity s WHERE s.name = :iName";
        Query query = em.createQuery(databaseQueryString);
        query.setParameter("iName", name);

        try {
            return (RoomTypeEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new RoomTypeNotFoundException("Room name " + name + " does not exist!");
        }

    }

    @Override
    public void deleteRoomType(String name) throws RoomTypeNotFoundException {
        RoomTypeEntity roomTypeToBeDeleted;
        try {
            roomTypeToBeDeleted = retrieveRoomType(name);
        } catch (RoomTypeNotFoundException ex) {
            throw new RoomTypeNotFoundException(ex.getMessage());
        }

        if (roomTypeEntityIsNotUsed(roomTypeToBeDeleted)) {
            em.remove(roomTypeToBeDeleted);
        } else {
            roomTypeToBeDeleted.setDisabled(Boolean.TRUE);
        }

    }

    @Override
    public void updateRoomType(String roomTypeName, RoomTypeEntity newRoomType) throws RoomTypeNotFoundException {
        try {
            RoomTypeEntity roomTypeToUpdate = retrieveRoomType(roomTypeName);
            roomTypeToUpdate = em.find(RoomTypeEntity.class, roomTypeToUpdate.getRoomTypeId());
            roomTypeToUpdate.setName(newRoomType.getName());
            roomTypeToUpdate.setDescription(newRoomType.getDescription());
            roomTypeToUpdate.setRoomSize(newRoomType.getRoomSize());
            roomTypeToUpdate.setBed(newRoomType.getBed());
            roomTypeToUpdate.setCapacity(newRoomType.getCapacity());
            roomTypeToUpdate.setAmenities(newRoomType.getAmenities());
            roomTypeToUpdate.setDisabled(newRoomType.getDisabled());

        } catch (RoomTypeNotFoundException ex) {
            throw new RoomTypeNotFoundException(ex.getMessage());
        }
    }

    private boolean roomTypeEntityIsNotUsed(RoomTypeEntity roomType) {
        return roomType.getRoomEntities().isEmpty();
    }

}
