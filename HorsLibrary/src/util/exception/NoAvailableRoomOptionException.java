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
public class NoAvailableRoomOptionException extends Exception{

    /**
     * Creates a new instance of <code>NoAvailableRoomOptionException</code>
     * without detail message.
     */
    public NoAvailableRoomOptionException() {
    }

    /**
     * Constructs an instance of <code>NoAvailableRoomOptionException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public NoAvailableRoomOptionException(String msg) {
        super(msg);
    }
}
