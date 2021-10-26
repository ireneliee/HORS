/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import entity.PartnerEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

/**
 *
 * @author irene
 */
@Stateless
public class PartnerEntitySessionBean implements PartnerEntitySessionBeanRemote, PartnerEntitySessionBeanLocal {

    @PersistenceContext(unitName = "Hors-ejbPU")
    private EntityManager em;

    public PartnerEntitySessionBean(){}
    
    @Override
    public Long createNewPartner(PartnerEntity newPartnerEntity) throws UsernameExistException,
            UnknownPersistenceException{
        try{
            em.persist(newPartnerEntity);
            em.flush();
            
            return newPartnerEntity.getUserId();
            
        } catch(PersistenceException ex){
        String databaseExceptionError = "org.eclipse.persistence.exceptions.DatabaseException";
            String similarUsernameError = "java.sql.SQLIntegrityConstraintViolationException";
            if(databaseExceptionChecker(ex, databaseExceptionError)) {
                 if(sameUsernameExceptionChecker(ex, similarUsernameError)) {
                     String usernameExistError = "Username exists within the system.";
                     throw new UsernameExistException(usernameExistError);
                 } else {
                     String unknownPersistenceError = "Unknown persistence exception.";
                     throw new UnknownPersistenceException(unknownPersistenceError);
                 }
            } else {
                String unknownPersistenceError = "Unknown persistence exception.";
                throw new UnknownPersistenceException(unknownPersistenceError);
            }
        }
    }
    
    @Override
    public PartnerEntity retrievePartnerByUsername(String username) throws PartnerNotFoundException {
        String databaseQuery = "SELECT s FROM PartnerEntity s WHERE s.username = :inUsername";
        Query query = em.createQuery(databaseQuery);
        query.setParameter("inUsername", username);
        
        try {
            return(PartnerEntity) query.getSingleResult();
        } catch(NoResultException | NonUniqueResultException ex) {
            String errorMessage = " Employee Username " + username + " does not exist!";
            throw new PartnerNotFoundException(errorMessage);
        }
    }
    
    @Override
    public PartnerEntity partnerLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            PartnerEntity partnerEntity = retrievePartnerByUsername(username);
            
            if(partnerEntity.getPassword().equals(password)) {
                partnerEntity.getRoomReservations().size();
                return partnerEntity;
            } else {
                String errorMessage = "Invalid password!";
                throw new InvalidLoginCredentialException(errorMessage);
            }
                
        } catch(PartnerNotFoundException ex){
            String errorMessage = "Username does not exist";
            throw new InvalidLoginCredentialException(errorMessage);
        }
    }
    
    
    
    private boolean databaseExceptionChecker(PersistenceException ex, String databaseExceptionError) {
        return ex.getCause() != null && ex.getCause().getClass().getName().equals(databaseExceptionError);
    }
    
    private boolean sameUsernameExceptionChecker(PersistenceException ex, String similarUsernameError) {
        return ex.getCause().getCause() != null &&
                ex.getCause().getCause().getClass().getName().equals(similarUsernameError);
    }
    
}
