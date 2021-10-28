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
import entity.RoomTypeEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.NormalRateHasAlreadyExistedException;
import util.exception.PeakRateHasAlreadyExistedException;
import util.exception.PromotionRateHasAlreadyExistedException;
import util.exception.PublishedRateHasAlreadyExistedException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author irene
 */
@Stateless
public class RoomRateEntitySessionBean implements RoomRateEntitySessionBeanRemote, RoomRateEntitySessionBeanLocal {

    @PersistenceContext(unitName = "Hors-ejbPU")
    private EntityManager em;

    public RoomRateEntitySessionBean() {
    }

    @Override
    public Long createNewPublishedRateEntity(PublishedRateEntity newPublishedRateEntity) throws PublishedRateHasAlreadyExistedException,
            UnknownPersistenceException {
        // have to check if the room has already have a published rate
        // one room type can only have one published rate
        RoomTypeEntity roomType = newPublishedRateEntity.getRoomType();
        String databaseQueryString = "SELECT s FROM RoomRateEntity s WHERE s.roomType = :iRoomType";
        Query databaseQuery = em.createQuery(databaseQueryString);
        databaseQuery.setParameter("iRoomType", roomType);

        Boolean publishedRateExistForTheRoomType = databaseQuery
                                                                                        .getResultList()
                                                                                        .stream()
                                                                                        .filter(x -> x instanceof PublishedRateEntity)
                                                                                        .findAny().isPresent();
        
        if(publishedRateExistForTheRoomType) {
            throw new PublishedRateHasAlreadyExistedException("Published rate has already existed! ");
        }
        
        try
            {
                em.persist(newPublishedRateEntity);
                em.flush();

                return newPublishedRateEntity.getRoomRateId();
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new PublishedRateHasAlreadyExistedException("Published rate has already existed.");
                    }
                    else
                    {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
   
    }
    
    @Override
    public Long createNewNormalRateEntity(NormalRateEntity newNormalRateEntity) throws NormalRateHasAlreadyExistedException,
            UnknownPersistenceException {
        // have to check if the room has already have a normal rate
        // one room type can only have one normal rate
        RoomTypeEntity roomType = newNormalRateEntity.getRoomType();
        String databaseQueryString = "SELECT s FROM RoomRateEntity s WHERE s.roomType = :iRoomType";
        Query databaseQuery = em.createQuery(databaseQueryString);
        databaseQuery.setParameter("iRoomType", roomType);

        Boolean normalRateExistForTheRoomType = databaseQuery
                                                                                        .getResultList()
                                                                                        .stream()
                                                                                        .filter(x -> x instanceof NormalRateEntity)
                                                                                        .findAny().isPresent();
        
        if(normalRateExistForTheRoomType) {
            throw new NormalRateHasAlreadyExistedException("Normal rate has already existed! ");
        }
        
        try
            {
                em.persist(newNormalRateEntity);
                em.flush();

                return newNormalRateEntity.getRoomRateId();
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new NormalRateHasAlreadyExistedException("Normal rate has already existed.");
                    }
                    else
                    {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
   
    }
    
    @Override
     public Long createNewPeakRateEntity(PeakRateEntity newPeakRateEntity) throws PeakRateHasAlreadyExistedException,
            UnknownPersistenceException {
         try
            {
                em.persist(newPeakRateEntity);
                em.flush();

                return newPeakRateEntity.getRoomRateId();
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new PeakRateHasAlreadyExistedException("Normal rate has already existed.");
                    }
                    else
                    {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
     }
     
    @Override
     public Long createNewPromotionRateEntity(PromotionRateEntity newPromotionRateEntity) throws PromotionRateHasAlreadyExistedException,
            UnknownPersistenceException {
         try
            {
                em.persist(newPromotionRateEntity);
                em.flush();

                return newPromotionRateEntity.getRoomRateId();
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new PromotionRateHasAlreadyExistedException("Normal rate has already existed.");
                    }
                    else
                    {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
     }
     
     
}
