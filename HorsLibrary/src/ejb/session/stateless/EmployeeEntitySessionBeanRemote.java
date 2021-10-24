/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.EmployeeEntity;
import javax.ejb.Remote;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

/**
 *
 * @author irene
 */
@Remote
public interface EmployeeEntitySessionBeanRemote {
    public Long createNewEmployee(EmployeeEntity newEmployeeEntity) throws UsernameExistException, UnknownPersistenceException;
    
}
