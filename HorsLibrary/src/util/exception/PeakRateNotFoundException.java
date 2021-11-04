/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author irene
 */
public class PeakRateNotFoundException extends Exception {
    public PeakRateNotFoundException(){}
    
    public PeakRateNotFoundException(String msg) {
        super(msg);
    }

}
