/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.GuestEntity;
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
import util.exception.GuestNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;



@Stateless
public class GuestEntitySessionBean implements GuestEntitySessionBeanRemote, GuestEntitySessionBeanLocal {

    @PersistenceContext(unitName = "Hors-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public GuestEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
  
    @Override
    public GuestEntity guestLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            GuestEntity guestEntity = retrieveGuestByUsername(username);
            
            if(guestEntity.getPassword().equals(password)) {
                guestEntity.getRoomReservations().size();
                return guestEntity;
            } else {
                String errorMessage = "Invalid password!";
                throw new InvalidLoginCredentialException(errorMessage);
            }
                
        } catch(GuestNotFoundException ex){
            String errorMessage = "Username does not exist";
            throw new InvalidLoginCredentialException(errorMessage);
        }
    }
 
    
    @Override
    public GuestEntity retrieveGuestByUsername(String username) throws GuestNotFoundException {
        String databaseQuery = "SELECT g FROM GuestEntity g WHERE g.username = :inUsername";
        Query query = em.createQuery(databaseQuery);
        query.setParameter("inUsername", username);
        
        try {
            return(GuestEntity) query.getSingleResult();
        } catch(NoResultException | NonUniqueResultException ex) {
            String errorMessage = " Guest Username " + username + " does not exist!";
            throw new GuestNotFoundException(errorMessage);
        }
    }
    
    
    public Long guestRegister(GuestEntity newGuestEntity) throws UsernameExistException, UnknownPersistenceException{
        Set<ConstraintViolation<GuestEntity>>constraintViolations = validator.validate(newGuestEntity);
        if(!constraintViolations.isEmpty()) {
            throw new UnknownPersistenceException("Wrong data input. Please check your data again.");
        }
            try {
                em.persist(newGuestEntity);
                em.flush();

                return newGuestEntity.getUserId();
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new UsernameExistException("Username, pasport number, or mobile phone exist in the system");
                    }
                    else
                    {
                        throw new UnknownPersistenceException("Unknown persist error");
                }
                }
                else
                {
                    throw new UnknownPersistenceException("Unknown persist error");
                }
            }

    }
}
