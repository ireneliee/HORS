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
public class RoomNumberExistException extends Exception{
    
    public RoomNumberExistException() {}
    
    public RoomNumberExistException(String msg){
        super(msg);
    }
    
}
