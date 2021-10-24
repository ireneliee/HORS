/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

/**
 *
 * @author irene
 */
@Stateless
public class EmployeeEntitySessionBean implements EmployeeEntitySessionBeanRemote,
        EmployeeEntitySessionBeanLocal {

    @PersistenceContext(unitName = "Hors-ejbPU")
    private EntityManager em;

    public EmployeeEntitySessionBean(){}
    
    @Override
    public Long createNewEmployee(EmployeeEntity newEmployeeEntity) throws UsernameExistException, 
            UnknownPersistenceException {
        try {
            em.persist(newEmployeeEntity);
            em.flush();
            
            return newEmployeeEntity.getUserId();
            
        } catch (PersistenceException ex) {
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
    public List<EmployeeEntity> retrieveAllEmployees() {
        String databaseQueryString = "SELECT s FROM EmployeeEntity s";
        Query databaseQuery = em.createQuery(databaseQueryString);
        return databaseQuery.getResultList();
        
    }
    
    @Override
    public EmployeeEntity retrieveEmployeeByEmployeeId(Long employeeId) throws EmployeeNotFoundException {
        EmployeeEntity employeeEntity = em.find(EmployeeEntity.class, employeeId);
        
        if(employeeEntity != null) {
            return employeeEntity;
        } else {
            String errorMessage = "Employee ID " + employeeId + " does not exist!"; 
            throw new EmployeeNotFoundException(errorMessage);
        }
    }
    
    @Override
    public EmployeeEntity retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException {
        String databaseQuery = "SELECT s FROM EmployeeEntity s WHERE s.username = :inUsername";
        Query query = em.createQuery(databaseQuery);
        query.setParameter("inUsername", username);
        
        try {
            return(EmployeeEntity) query.getSingleResult();
        } catch(NoResultException | NonUniqueResultException ex) {
            String errorMessage = " Employee Username " + username + " does not exist!";
            throw new EmployeeNotFoundException(errorMessage);
        }
    }
    
    @Override
    public EmployeeEntity employeeLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            EmployeeEntity employeeEntity = retrieveEmployeeByUsername(username);
            
            if(employeeEntity.getPassword().equals(password)) {
                employeeEntity.getRoomReservations().size();
                return employeeEntity;
            } else {
                String errorMessage = "Invalid password!";
                throw new InvalidLoginCredentialException(errorMessage);
            }
                
        } catch(EmployeeNotFoundException ex){
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
