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
public class RoomAllocationIsDoneException extends Exception {
    public RoomAllocationIsDoneException(){}
    
    public RoomAllocationIsDoneException(String msg) {
        super(msg);
    }
}
