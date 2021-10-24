
package ejb.session.stateless;

import entity.EmployeeEntity;
import javax.ejb.Local;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

/**
 *
 * @author irene
 */
@Local
public interface HorsManagementControllerSessionBeanLocal {

    public Long createNewEmployee(EmployeeEntity newEmployeeEntity) throws UnknownPersistenceException, UsernameExistException;

    public EmployeeEntity employeeLogin(String username, String password) throws InvalidLoginCredentialException;
    
}
