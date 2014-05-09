/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processing;

import game.core.Core;
import java.awt.event.KeyEvent;
import listener.ButtonEvent;
import game.listener.MultiKeyListener;
import processing.core.PApplet;

/**
 *
 * @author tobias
 */
public class TankWar extends PApplet {

    private MultiKeyListener keyListener = new MultiKeyListener();

    private Core core;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PApplet.main(new String[]{"processing.TankWar"});
    }

    public void setup() {
        size(1300,700);
        core = new Core();
        keyListener.addPressedEvent(new ButtonEvent(KeyEvent.VK_RIGHT) {
            @Override
            public void keyAction() {
                if (!core.getPlayer().isDead()) {
                    core.getPlayer().addRotation(0.2f);
                }
            }
        });
        keyListener.addPressedEvent(new ButtonEvent(KeyEvent.VK_LEFT) {
            @Override
            public void keyAction() {
                if (!core.getPlayer().isDead()) {
                    core.getPlayer().addRotation(-0.2f);
                }
            }
        });
        keyListener.addPressedEvent(new ButtonEvent(KeyEvent.VK_UP) {
            @Override
            public void keyAction() {
                if (!core.getPlayer().isDead()) {
                    core.getPlayer().setAcceleration(0.5f);
                    core.getPlayer().setAccelerating(true);
                }
            }
        });
        keyListener.addPressedEvent(new ButtonEvent(KeyEvent.VK_DOWN) {
            @Override
            public void keyAction() {
                if (!core.getPlayer().isDead()) {
                    core.getPlayer().setAcceleration(-0.5f);
                    core.getPlayer().setAccelerating(true);
                }
            }
        });
        keyListener.addReleaseEvent(new ButtonEvent(KeyEvent.VK_UP) {         
            @Override
            public void keyAction() {
                core.getPlayer().setAccelerating(false);
            }
        });
        keyListener.addReleaseEvent(new ButtonEvent(KeyEvent.VK_DOWN) {         
            @Override
            public void keyAction() {
                core.getPlayer().setAccelerating(false);
            }
        });
        keyListener.addPressedEvent(new ButtonEvent(KeyEvent.VK_SPACE) {
            @Override
            public void keyAction() {
                if (!core.getPlayer().isDead()) {
                    core.getPlayer().fire();
                }
            }
        });
        keyListener.addReleaseEvent(new ButtonEvent(KeyEvent.VK_SPACE) {
            @Override
            public void keyAction() {
                //player.fireD();
            }
        });
        keyListener.addReleaseEvent(new ButtonEvent(KeyEvent.VK_R) {
            @Override
            public void keyAction() {
                core.getPlayer().reload();
            }
        });
        keyListener.addPressedEvent(new ButtonEvent(KeyEvent.VK_Q) {
            @Override
            public void keyAction() {
                core.getPlayer().setRotationIntervall(262);
            }
        });
        keyListener.addReleaseEvent(new ButtonEvent(KeyEvent.VK_Q) {
            @Override
            public void keyAction() {
                core.getPlayer().setRotationIntervall(0);
            }
        });
//        System.out.println("neue id: " + newID);
//        playerId = newID;
//        player = new Tank(400, 300, 0, newID);
//        Random r = new Random();
//        walls.add(new Wall(100, 100, 200, 50, r.nextInt(360), 0));
//        walls.add(new Wall(300, 100, 200, 50, r.nextInt(360), 0));
//        walls.add(new Wall(500, 100, 200, 50, r.nextInt(360), 0));
//        walls.add(new Wall(700, 100, 200, 50, r.nextInt(360), 0));
//        walls.add(new Wall(100, 500, 200, 50, r.nextInt(360), 0));
//        walls.add(new Wall(300, 500, 200, 50, r.nextInt(360), 0));
//        walls.add(new Wall(500, 500, 200, 50, r.nextInt(360), 0));
//        walls.add(new Wall(700, 500, 200, 50, r.nextInt(360), 0));
//        walls.add(new Wall(825, 200, 200, 50, r.nextInt(360), 0));
//        walls.add(new Wall(825, 400, 200, 50, r.nextInt(360), 0));
//        walls.add(new Wall(100, 300, 200, 50, r.nextInt(360), 1));
    }

    public void draw() {
        clear();
        background(0, 0, 0, 255);
        core.tick();
        core.draw(this);
        text("FPS: " + frameRate, 10, 10);
        text(core.getHost().getType(), 10, 30);
    }

    public void keyPressed() {
        keyListener.keyPressed(keyCode);
    }

    public void keyReleased() {
        keyListener.keyReleased(keyCode);
    }

}
