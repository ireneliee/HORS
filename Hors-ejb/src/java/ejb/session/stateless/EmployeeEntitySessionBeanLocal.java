/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

/**
 *
 * @author irene
 */
@Local
public interface EmployeeEntitySessionBeanLocal {

    public Long createNewEmployee(EmployeeEntity newEmployeeEntity) throws UsernameExistException, UnknownPersistenceException;

    public List<EmployeeEntity> retrieveAllEmployees();

    public EmployeeEntity retrieveEmployeeByEmployeeId(Long employeeId) throws EmployeeNotFoundException;

    public EmployeeEntity retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException;

    public EmployeeEntity employeeLogin(String username, String password) throws InvalidLoginCredentialException;
    
}
