/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeEntitySessionBeanLocal;
import ejb.session.stateless.GuestEntitySessionBeanLocal;
import ejb.session.stateless.RoomEntitySessionBeanLocal;
import ejb.session.stateless.RoomReservationEntitySessionBeanLocal;
import ejb.session.stateless.RoomTypeEntitySessionBeanLocal;
import entity.EmployeeEntity;
import entity.GuestEntity;
import entity.RoomReservationEntity;
import entity.RoomReservationLineItemEntity;
import entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.AccessRightEnum;
import util.exception.InvalidRoomReservationEntityException;
import util.exception.RoomTypeExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UsernameExistException;

/**
 * dummy files for testing - loading data test
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private RoomReservationEntitySessionBeanLocal roomReservationEntitySessionBean;

    @EJB
    private RoomEntitySessionBeanLocal roomEntitySessionBean;

    @EJB
    private RoomTypeEntitySessionBeanLocal roomTypeEntitySessionBean;

    @EJB
    private EmployeeEntitySessionBeanLocal employeeEntitySessionBean;

    @EJB
    private GuestEntitySessionBeanLocal guestEntitySessionBean;

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

        
        if (em.find(GuestEntity.class, 1L) == null) {
            doDataInitialisationGuestEntity();
        } 
         
    }

    private void doDataInitialisationRoomTypeEntity() {
        try {
            // roomType creation
            RoomTypeEntity roomTypeOne = new RoomTypeEntity("Deluxe", "Basic room", "10x8", 2, 2, "wifi", 1);

            RoomTypeEntity roomTypeTwo = new RoomTypeEntity("Premium", "Better room", "12x8", 2, 2, "bathtub", 2);

            RoomTypeEntity roomTypeThree = new RoomTypeEntity("Premium plus", "Much better room", "12x10", 2, 3, "bathtub + sofa", 3);

            RoomTypeEntity roomTypeFour = new RoomTypeEntity("Queen", "Much better room", "12x10", 3, 3, "bathtub + sofa", 4);

            RoomTypeEntity roomTypeFive = new RoomTypeEntity("King", "Much better room", "12x14", 2, 3, "bathtub + sofa", 5);

            roomTypeEntitySessionBean.createRoomType(roomTypeOne);
            roomTypeEntitySessionBean.createRoomType(roomTypeTwo);
            roomTypeEntitySessionBean.createRoomType(roomTypeThree);
            roomTypeEntitySessionBean.createRoomType(roomTypeFour);
            roomTypeEntitySessionBean.createRoomType(roomTypeFive);

           
            // room reservation created. 4 deluxe room, 2 premium room, all in the same check in and check out date :)
            // reservation 1
            RoomReservationLineItemEntity roomOne = new RoomReservationLineItemEntity(roomTypeOne, new BigDecimal(100000),
                    LocalDate.of(2021, 11, 3), LocalDate.of(2021, 11, 6));

            RoomReservationLineItemEntity roomTwo = new RoomReservationLineItemEntity(roomTypeOne, new BigDecimal(100000),
                    LocalDate.of(2021, 11, 3), LocalDate.of(2021, 11, 6));

            RoomReservationEntity roomReservationOne = new RoomReservationEntity(new BigDecimal(200000), LocalDate.of(2021, 10, 10));
            roomReservationOne.getRoomReservationLineItems().add(roomOne);
            roomReservationOne.getRoomReservationLineItems().add(roomTwo);

            // reservation 2
            RoomReservationLineItemEntity roomThree = new RoomReservationLineItemEntity(roomTypeOne, new BigDecimal(100000),
                    LocalDate.of(2021, 11, 10), LocalDate.of(2021, 11, 16));

            RoomReservationLineItemEntity roomFour = new RoomReservationLineItemEntity(roomTypeOne, new BigDecimal(100000),
                    LocalDate.of(2021, 11, 10), LocalDate.of(2021, 11, 16));

            RoomReservationEntity roomReservationTwo = new RoomReservationEntity(new BigDecimal(200000), LocalDate.of(2021, 10, 10));
            roomReservationTwo.getRoomReservationLineItems().add(roomThree);
            roomReservationTwo.getRoomReservationLineItems().add(roomFour);

            // reservation 3
            RoomReservationLineItemEntity roomFive = new RoomReservationLineItemEntity(roomTypeTwo, new BigDecimal(100000),
                    LocalDate.of(2021, 11, 10), LocalDate.of(2021, 11, 16));

            RoomReservationLineItemEntity roomSix = new RoomReservationLineItemEntity(roomTypeTwo, new BigDecimal(100000),
                    LocalDate.of(2021, 11, 10), LocalDate.of(2021, 11, 16));

            RoomReservationEntity roomReservationThree = new RoomReservationEntity(new BigDecimal(200000), LocalDate.of(2021, 10, 10));
            roomReservationThree.getRoomReservationLineItems().add(roomFive);
            roomReservationThree.getRoomReservationLineItems().add(roomSix);

            try {
                roomReservationEntitySessionBean.createNewRoomReservationEntity(roomReservationOne);
                roomReservationEntitySessionBean.createNewRoomReservationEntity(roomReservationTwo);
                roomReservationEntitySessionBean.createNewRoomReservationEntity(roomReservationThree);
            } catch (InvalidRoomReservationEntityException ex) {
                System.out.print("Error has occured!");
            }
            
            /*
            roomEntitySessionBean.createNewRoom(roomA, roomTypeOne);
            roomEntitySessionBean.createNewRoom(roomB, roomTypeOne);
            roomEntitySessionBean.createNewRoom(roomC, roomTypeOne);
            roomEntitySessionBean.createNewRoom(roomD, roomTypeTwo);
            roomEntitySessionBean.createNewRoom(roomE, roomTypeTwo);
            roomEntitySessionBean.createNewRoom(roomF, roomTypeTwo);
            roomEntitySessionBean.createNewRoom(roomG, roomTypeFour);
            */

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
            
            EmployeeEntity employeeFive = new EmployeeEntity("Employee", "Five", "employeeFive", "password",
                    AccessRightEnum.GUESTRELATIONOFFICER);
            employeeEntitySessionBean.createNewEmployee(employeeFour);

        } catch (UsernameExistException | UnknownPersistenceException ex) {
            System.out.println(ex.getMessage());
        }
    }

    
    private void doDataInitialisationGuestEntity() {
            try {   
            
            //Create guest
            GuestEntity guestOne = new GuestEntity("Guest","One", "guestOne","password", "email1", "11111","1111");
            guestEntitySessionBean.guestRegister(guestOne);
            
            GuestEntity guestTwo = new GuestEntity("Guest","Two", "guestTwo","password", "email2", "22222","2222");
            guestEntitySessionBean.guestRegister(guestTwo);
            
            GuestEntity guestThree = new GuestEntity("Guest","Three", "guestThree","password", "email3", "33333","3333");
            guestEntitySessionBean.guestRegister(guestThree);
            
            } catch (UsernameExistException | UnknownPersistenceException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
