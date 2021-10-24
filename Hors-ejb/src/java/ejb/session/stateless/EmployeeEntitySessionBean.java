/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

/**
 *
 * @author irene
 */
@Stateless
public class EmployeeEntitySessionBean implements EmployeeEntitySessionBeanRemote, EmployeeEntitySessionBeanLocal {

    @PersistenceContext(unitName = "Hors-ejbPU")
    private EntityManager em;

    public EmployeeEntitySessionBean(){}
    
    @Override
    public Long createNewEmployee(EmployeeEntity newEmployeeEntity) throws UsernameExistException, UnknownPersistenceException {
        try {
            em.persist(newEmployeeEntity);
            em.flush();
            
            return newEmployeeEntity.getUserId();
            
        } catch (PersistenceException ex) {
            String databaseExceptionError = "org.eclipse.persistence.exceptions.DatabaseException";
            String similarUsernameError = "java.sql.SQLIntegrityConstraintViolationException";
            if(databaseExceptionChecker(ex, databaseExceptionError)) {
                 if(sameUsernameExceptionChecker(ex, similarUsernameError)) {
                     throw new UsernameExistException(ex.getMessage());
                 } else {
                     throw new UnknownPersistenceException(ex.getMessage());
                 }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    private boolean databaseExceptionChecker(PersistenceException ex, String databaseExceptionError) {
        return ex.getCause() != null && ex.getCause().getClass().getName().equals(databaseExceptionError);
    }
    
    private boolean sameUsernameExceptionChecker(PersistenceException ex, String similarUsernameError) {
        return ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals(similarUsernameError);
    }
}
