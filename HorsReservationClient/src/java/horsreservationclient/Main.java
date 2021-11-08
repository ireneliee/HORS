/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsreservationclient;

import ejb.session.stateful.ReserveOperationSessionBeanRemote;
import ejb.session.stateless.GuestEntitySessionBeanRemote;
import ejb.session.stateless.HolidayReservationSystemControllerRemote;
import ejb.session.stateless.HorsReservationClientControllerRemote;
import javax.ejb.EJB;

/**
 *
 * @author irene
 */
public class Main {

   

    @EJB
    private static HorsReservationClientControllerRemote horsReservationClientController;

    
    
    
    
        

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
       
        
        MainApp mainApp = new MainApp(horsReservationClientController);
        mainApp.runApp();
        
        
    }
    
}
