/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package py.smtr.web.shared;
 
/**
 *
 * @author Strogg
 */
public class WARException extends Exception {
 
    public WARException(String message) {
        super(message);
    }
 
    public WARException() {
    }
 
    public WARException(Throwable cause) {
        super(cause);
    }
    
}