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
public class RoomNumberDoesNotExist extends Exception {
    public RoomNumberDoesNotExist(){}
    
    public RoomNumberDoesNotExist(String msg) {
        super(msg);
    }
}
