/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author irene
 */
public class GuestHasNotCheckedInException extends Exception {
    public GuestHasNotCheckedInException(){}
    
    public GuestHasNotCheckedInException(String msg) {
        super(msg);
    }
}
