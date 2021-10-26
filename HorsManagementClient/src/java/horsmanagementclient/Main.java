/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horsmanagementclient;

import ejb.session.stateless.HorsManagementControllerSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author irene
 */
public class Main {

    @EJB
    private static HorsManagementControllerSessionBeanRemote horsManagementControllerSessionBeanRemote;

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
       MainApp mainApp = new MainApp(horsManagementControllerSessionBeanRemote);
       mainApp.runApp();
    }
    
}
