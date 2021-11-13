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
import entity.RoomRateEntity;
import entity.RoomReservationLineItemEntity;
import entity.RoomTypeEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.DeleteRoomRateException;
import util.exception.NormalRateHasAlreadyExistedException;
import util.exception.PeakRateHasAlreadyExistedException;
import util.exception.PromotionRateHasAlreadyExistedException;
import util.exception.PublishedRateHasAlreadyExistedException;
import util.exception.RoomRateEntityNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author irene
 */
@Stateless
public class RoomRateEntitySessionBean implements RoomRateEntitySessionBeanRemote, RoomRateEntitySessionBeanLocal {

    @PersistenceContext(unitName = "Hors-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public RoomRateEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewPublishedRateEntity(PublishedRateEntity newPublishedRateEntity) throws PublishedRateHasAlreadyExistedException,
            UnknownPersistenceException {

        Set<ConstraintViolation<RoomRateEntity>> constraintViolations = validator.validate(newPublishedRateEntity);
        if (!constraintViolations.isEmpty()) {
            throw new UnknownPersistenceException("Wrong data input.");
        }
        try {
            em.persist(newPublishedRateEntity);
            em.flush();

            return newPublishedRateEntity.getRoomRateId();
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException("A persistence exception has happened.");

        }
    }

    @Override
    public Long createNewNormalRateEntity(NormalRateEntity newNormalRateEntity) throws NormalRateHasAlreadyExistedException,
            UnknownPersistenceException {
        Set<ConstraintViolation<RoomRateEntity>> constraintViolations = validator.validate(newNormalRateEntity);
        if (!constraintViolations.isEmpty()) {
            throw new UnknownPersistenceException("Wrong data input.");
        }

        try {
            em.persist(newNormalRateEntity);
            em.flush();

            return newNormalRateEntity.getRoomRateId();
        } catch (PersistenceException ex) {
            throw new PersistenceException("A persistence error occurs.");
        }

    }

    @Override
    public Long createNewPeakRateEntity(PeakRateEntity newPeakRateEntity) throws PeakRateHasAlreadyExistedException,
            UnknownPersistenceException {
        Set<ConstraintViolation<RoomRateEntity>> constraintViolations = validator.validate(newPeakRateEntity);
        if (!constraintViolations.isEmpty()) {
            throw new UnknownPersistenceException("Wrong data input.");
        }
        em.persist(newPeakRateEntity);
        em.flush();

        return newPeakRateEntity.getRoomRateId();

    }

    @Override
    public Long createNewPromotionRateEntity(PromotionRateEntity newPromotionRateEntity) throws PromotionRateHasAlreadyExistedException,
            UnknownPersistenceException {
        Set<ConstraintViolation<RoomRateEntity>> constraintViolations = validator.validate(newPromotionRateEntity);
        if (!constraintViolations.isEmpty()) {
            throw new UnknownPersistenceException("Wrong data input.");
        }
        try {
            em.persist(newPromotionRateEntity);
            em.flush();

            return newPromotionRateEntity.getRoomRateId();
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException("Unknown persistence has occured.");
        }
    }

    @Override
    public void updatePublishedAndNormalRate(RoomRateEntity roomRate
    ) {
        RoomRateEntity roomRateToBeUpdated = em.find(RoomRateEntity.class, roomRate.getRoomRateId());
        roomRateToBeUpdated.setRate(roomRate.getRate());
    }

    @Override
    public void updatePromotionAndPeakRate(RoomRateEntity roomRate
    ) {
        RoomRateEntity roomRateToBeUpdated = em.find(RoomRateEntity.class, roomRate.getRoomRateId());
        roomRateToBeUpdated.setStartValidityDate(roomRate.getStartValidityDate());
        roomRateToBeUpdated.setEndValidityDate(roomRate.getEndValidityDate());
        roomRateToBeUpdated.setRate(roomRate.getRate());
    }

    @Override
    public List<RoomRateEntity> retrieveAllRoomRate() {
        String queryString = "SELECT s FROM RoomRateEntity s";
        Query query = em.createQuery(queryString);
        return query.getResultList();
    }

    @Override
    public void deleteRoomRateEntity(RoomRateEntity roomRate) throws DeleteRoomRateException {
        RoomRateEntity roomRateToBeRemoved = em.find(RoomRateEntity.class, roomRate.getRoomRateId());
 
        if (roomRate instanceof PublishedRateEntity || roomRate instanceof NormalRateEntity) {
            //roomRateToBeRemoved.setDisabled(true);
            throw new DeleteRoomRateException("Published and Normal rate entity cannot be removed. Update room rate instead.");
        }
        em.remove(roomRateToBeRemoved);

    }


    @Override
    public RoomRateEntity retrieveRoomRateDetails(Long roomRateId)
            throws RoomRateEntityNotFoundException {
        try {
            RoomRateEntity roomRateToBeFound = em.find(RoomRateEntity.class, roomRateId);
            return roomRateToBeFound;
        } catch (NoResultException ex) {
            throw new RoomRateEntityNotFoundException("Room rate id not found.");
        }
    }

}
