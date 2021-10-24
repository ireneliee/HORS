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
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
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
                     throw new UsernameExistException("Username exists within the system.");
                 } else {
                     throw new UnknownPersistenceException("Unknown persistence exception.");
                 }
            } else {
                throw new UnknownPersistenceException("Unknown persistence exception.");
            }
        }
    }
    
    @Override
    public List<EmployeeEntity> retrieveAllEmployees() {
        String databaseQueryString = "SELECT s FROM EmployeeEntity s";
        Query databaseQuery = em.createQuery(databaseQueryString);
        return databaseQuery.getResultList();
        
    }
    
    private boolean databaseExceptionChecker(PersistenceException ex, String databaseExceptionError) {
        return ex.getCause() != null && ex.getCause().getClass().getName().equals(databaseExceptionError);
    }
    
    private boolean sameUsernameExceptionChecker(PersistenceException ex, String similarUsernameError) {
        return ex.getCause().getCause() != null &&
                ex.getCause().getCause().getClass().getName().equals(similarUsernameError);
    }
}
