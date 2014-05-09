/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import listener.ButtonEvent;

/**
 *
 * @author Tobi
 */
public class MultiKeyListener extends Thread {

    private ArrayList<ButtonEvent> occupiedKeysPressed = new ArrayList();
    private ArrayList<ButtonEvent> occupiedKeysReleased = new ArrayList();
    private ArrayList<Integer> pressedKeys = new ArrayList();
    private boolean run = true;

    public MultiKeyListener() {
        this.start();
    }

    public void keyPressed(int keyCode) {
        if (!keyIsPressed(keyCode)) {
            pressedKeys.add(keyCode);
        }
    }
    
    public void keyReleased(int keyCode) {
        if (pressedKeys.size() > 0) {
            int i = 0;
            for (Integer key : pressedKeys) {
                if (key == keyCode) {
                    pressedKeys.remove(i);
                    break;
                }
                i++;
            }
        }
        try {
            for (ButtonEvent event : occupiedKeysReleased) {
                if (event.getKeyCode() == keyCode) {
                    event.keyAction();
                }
            }
        } catch (Exception ex) {
        }
    }

    public void addPressedEvent(ButtonEvent e) {
        this.occupiedKeysPressed.add(e);
    }

    public void addReleaseEvent(ButtonEvent e) {
        this.occupiedKeysReleased.add(e);
    }

    public void run() {
        while (run) {
            doPressedEvents();
            try {
                Thread.sleep(1);
            } catch (Exception e) {
            }
        }
    }

    private void doPressedEvents() {
        try {
            for (Integer key : pressedKeys) {
                for (ButtonEvent event : occupiedKeysPressed) {
                    if (event.getKeyCode() == key) {
                        event.keyAction();
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private boolean keyIsPressed(int keyCode) {
        for (Integer key : pressedKeys) {
            if (key == keyCode) {
                return true;
            }
        }
        return false;
    }
    
    public void changeReleaseKeyAction(ButtonEvent event) {
        try {
            for (int i = 0;i<occupiedKeysReleased.size();i++) {
                if(occupiedKeysReleased.get(i).getKeyCode()==event.getKeyCode()) {
                    occupiedKeysReleased.set(i, event);
                    return;
                }
            }
        } catch (Exception e) {}
    }
    
    public void changePressKeyAction(ButtonEvent event) {
        try {
            for (int i = 0;i<occupiedKeysPressed.size();i++) {
                if(occupiedKeysPressed.get(i).getKeyCode()==event.getKeyCode()) {
                    occupiedKeysPressed.set(i, event);
                    return;
                }
            }
        } catch (Exception e) {}
    }
    
    public void changePressKey(int oldKeyCode, int newKeyCode) {
        try {
            for (int i = 0;i<occupiedKeysPressed.size();i++) {
                if(occupiedKeysPressed.get(i).getKeyCode()==oldKeyCode) {
                    occupiedKeysPressed.get(i).setKeyCode(newKeyCode);
                    return;
                }
            }
        } catch (Exception e) {}
    }
}
