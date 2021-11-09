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
public class LineItemExistException extends Exception{

    /**
     * Creates a new instance of <code>LineItemExistException</code> without
     * detail message.
     */
    public LineItemExistException() {
    }

    /**
     * Constructs an instance of <code>LineItemExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public LineItemExistException(String msg) {
        super(msg);
    }
}
