/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeEntitySessionBeanLocal;
import ejb.session.stateless.RoomEntitySessionBeanLocal;
import ejb.session.stateless.RoomTypeEntitySessionBeanLocal;
import entity.EmployeeEntity;
import entity.RoomEntity;
import entity.RoomTypeEntity;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.AccessRightEnum;
import util.enumeration.RoomStatusEnum;
import util.exception.InputDataValidationException;
import util.exception.RoomNumberExistException;
import util.exception.RoomTypeExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

/**
 * dummy files for testing - loading data
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private RoomEntitySessionBeanLocal roomEntitySessionBean;

    @EJB
    private RoomTypeEntitySessionBeanLocal roomTypeEntitySessionBean;

    @EJB
    private EmployeeEntitySessionBeanLocal employeeEntitySessionBean;

    @PersistenceContext(unitName = "Hors-ejbPU")
    private EntityManager em;

    @PostConstruct
    public void postConstruct() {
        if (em.find(EmployeeEntity.class, 1L) == null) {
            doDataInitialisationEmployee();
        }

        if (em.find(RoomTypeEntity.class, 1L) == null) {
            doDataInitialisationRoomTypeEntity();
        }
    }

    private void doDataInitialisationRoomTypeEntity() {
        try {
            RoomTypeEntity roomTypeOne = new RoomTypeEntity("Deluxe", "Basic room", "10x8", 2, 2, "wifi", 1);
            roomTypeEntitySessionBean.createRoomType(roomTypeOne);

            RoomTypeEntity roomTypeTwo = new RoomTypeEntity("Premium", "Better room", "12x8", 2, 2, "bathtub", 2);
            roomTypeEntitySessionBean.createRoomType(roomTypeTwo);

            RoomTypeEntity roomTypeThree = new RoomTypeEntity("Premium plus", "Much better room", "12x10", 2, 3, "bathtub + sofa", 3);
            roomTypeEntitySessionBean.createRoomType(roomTypeThree);

            RoomTypeEntity roomTypeFour = new RoomTypeEntity("Queen", "Much better room", "12x10", 3, 3, "bathtub + sofa", 4);
            roomTypeEntitySessionBean.createRoomType(roomTypeFour);

            RoomTypeEntity roomTypeFive = new RoomTypeEntity("King", "Much better room", "12x14", 2, 3, "bathtub + sofa", 5);
            roomTypeEntitySessionBean.createRoomType(roomTypeFive);

            try {
                RoomEntity roomOne = new RoomEntity(2015, RoomStatusEnum.AVAILABLE, roomTypeOne);
                roomEntitySessionBean.createNewRoom(roomOne);
                
                RoomEntity roomTwo = new RoomEntity(3015, RoomStatusEnum.AVAILABLE, roomTypeTwo);
                roomEntitySessionBean.createNewRoom(roomTwo);
                
                RoomEntity roomThree = new RoomEntity(4015, RoomStatusEnum.AVAILABLE, roomTypeThree);
                roomEntitySessionBean.createNewRoom(roomThree);
                
                RoomEntity roomFour = new RoomEntity(5015, RoomStatusEnum.AVAILABLE, roomTypeFour);
                roomEntitySessionBean.createNewRoom(roomFour);
                
                RoomEntity roomFive = new RoomEntity(6015, RoomStatusEnum.NOTAVAILABLE, roomTypeFive);
                roomEntitySessionBean.createNewRoom(roomFive);
                
            } catch (RoomNumberExistException | UnknownPersistenceException | InputDataValidationException ex) {
                System.out.print(ex.getMessage());
            }

        } catch (RoomTypeExistException | UnknownPersistenceException ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void doDataInitialisationEmployee() {
        try {
            EmployeeEntity employeeOne = new EmployeeEntity("Employee", "One", "employeeOne", "password",
                    AccessRightEnum.SYSTEMADMINISTRATOR);
            employeeEntitySessionBean.createNewEmployee(employeeOne);

            EmployeeEntity employeeTwo = new EmployeeEntity("Employee", "Two", "employeeTwo", "password",
                    AccessRightEnum.SYSTEMADMINISTRATOR);
            employeeEntitySessionBean.createNewEmployee(employeeTwo);

            EmployeeEntity employeeThree = new EmployeeEntity("Employee", "Three", "employeeThree", "password",
                    AccessRightEnum.OPERATIONMANAGER);
            employeeEntitySessionBean.createNewEmployee(employeeThree);

            EmployeeEntity employeeFour = new EmployeeEntity("Employee", "Four", "employeeFour", "password",
                    AccessRightEnum.SALESMANAGER);
            employeeEntitySessionBean.createNewEmployee(employeeFour);
        } catch (UsernameExistException | UnknownPersistenceException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
