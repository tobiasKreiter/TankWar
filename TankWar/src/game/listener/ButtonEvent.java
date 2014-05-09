/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package listener;

import java.awt.event.KeyEvent;

/**
 *
 * @author Tobi
 */
public abstract class ButtonEvent {

    private int keyCode;
    
    public ButtonEvent(int keyCode) {
        this.keyCode = keyCode;
    }
    
    public abstract void keyAction();
    
    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }
    
    public int getKeyCode() {
        return keyCode;
    }
    
}
