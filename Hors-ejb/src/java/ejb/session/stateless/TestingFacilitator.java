/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomEntity;
import entity.RoomTypeEntity;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.RoomStatusEnum;
import util.exception.InputDataValidationException;
import util.exception.RoomNumberExistException;
import util.exception.RoomTypeNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author irene
 */
@Stateless
@LocalBean
public class TestingFacilitator {

    @EJB
    private RoomEntitySessionBeanLocal roomEntitySessionBean;

    @EJB
    private RoomTypeEntitySessionBeanLocal roomTypeEntitySessionBean;

    @PersistenceContext(unitName = "Hors-ejbPU")
    private EntityManager em;

    public TestingFacilitator() {
    }

    // room creation - 3 basic, 2 premium (1 not available), 1 Queen
    @Schedule(hour = "*", minute = "*", second = "*/5", info = "toFacilitateRoomReservationAllocation")
    public void doDataInitialisationRoomEntity() {
        Query query = em.createQuery("SELECT s FROM RoomEntity s");
        if (query.getResultList().isEmpty()) {
            doDataInitialisationRoomEntity();
        }
    }

    public void initiateRoomEntity() {
        try {
            RoomTypeEntity roomTypeOne = roomTypeEntitySessionBean.retrieveRoomType("Deluxe");
            
            RoomTypeEntity roomTypeTwo = roomTypeEntitySessionBean.retrieveRoomType("Premium");
            
            RoomTypeEntity roomTypeFour = roomTypeEntitySessionBean.retrieveRoomType("Queen");
            
            System.out.println("Done here A");
            RoomEntity roomA = new RoomEntity(2015, RoomStatusEnum.AVAILABLE, roomTypeOne);

            RoomEntity roomB = new RoomEntity(3015, RoomStatusEnum.AVAILABLE, roomTypeOne);

            RoomEntity roomC = new RoomEntity(4015, RoomStatusEnum.AVAILABLE, roomTypeOne);

            RoomEntity roomD = new RoomEntity(5015, RoomStatusEnum.AVAILABLE, roomTypeTwo);

            RoomEntity roomE = new RoomEntity(6015, RoomStatusEnum.NOTAVAILABLE, roomTypeTwo);

            RoomEntity roomF = new RoomEntity(7015, RoomStatusEnum.AVAILABLE, roomTypeTwo);

            RoomEntity roomG = new RoomEntity(8015, RoomStatusEnum.AVAILABLE, roomTypeFour);
            System.out.println("Done here B");
            try {
                roomEntitySessionBean.createNewRoom(roomA);
                roomEntitySessionBean.createNewRoom(roomB);
                roomEntitySessionBean.createNewRoom(roomC);
                roomEntitySessionBean.createNewRoom(roomD);
                roomEntitySessionBean.createNewRoom(roomE);
                roomEntitySessionBean.createNewRoom(roomF);
                roomEntitySessionBean.createNewRoom(roomG);
            } catch (RoomNumberExistException | UnknownPersistenceException | InputDataValidationException ex) {
                System.out.println("too bad");
            }
            System.out.println("Done here C");
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("too bad");
        }

    }

}
