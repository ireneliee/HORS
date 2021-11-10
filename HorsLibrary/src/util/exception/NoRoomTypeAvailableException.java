/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author zenyew
 */
public class NoRoomTypeAvailableException extends Exception{

    /**
     * Creates a new instance of <code>NoRoomTypeAvailableException</code>
     * without detail message.
     */
    public NoRoomTypeAvailableException() {
    }

    /**
     * Constructs an instance of <code>NoRoomTypeAvailableException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public NoRoomTypeAvailableException(String msg) {
        super(msg);
    }
}
