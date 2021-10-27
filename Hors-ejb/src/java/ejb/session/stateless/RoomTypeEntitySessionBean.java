/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomTypeEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.RoomNotFoundException;
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

    public RoomTypeEntitySessionBean(){}
    
    @Override
    public Long createRoomType(RoomTypeEntity newRoomType) throws RoomTypeExistException, 
            UnknownPersistenceException {
        try {
            em.persist(newRoomType);
            em.flush();
            return newRoomType.getRoomTypeId();
            
        } catch (PersistenceException ex) {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new RoomTypeExistException("Room type has already existed.");
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
        
        try{
            return (RoomTypeEntity)query.getSingleResult();
        } catch(NoResultException | NonUniqueResultException ex) {
            throw new RoomTypeNotFoundException("Room name " + name + " does not exist!");
        }
        
    }
    
    @Override
    public void deleteRoomType(String name) throws RoomTypeNotFoundException {
        RoomTypeEntity roomTypeToBeDeleted;
        try{
        roomTypeToBeDeleted = retrieveRoomType(name);
        } catch(RoomTypeNotFoundException ex) {
            throw new RoomTypeNotFoundException(ex.getMessage());
        }
        em.remove(roomTypeToBeDeleted);
        

        
        
    }

}
