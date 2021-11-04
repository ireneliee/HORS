/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystemclient;

import ejb.session.stateless.HolidayReservationSystemControllerRemote;
import ejb.session.stateless.PartnerEntitySessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author irene
 */
public class Main {

    @EJB
    private static HolidayReservationSystemControllerRemote holidayReservationSystemController;

    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        MainApp mainApp = new MainApp(holidayReservationSystemController);
        mainApp.runApp();
    }
    
}
