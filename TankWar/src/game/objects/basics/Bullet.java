/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.objects.basics;

import game.objects.GameObject;
import network.ObjectParser;
import processing.core.PApplet;

/**
 *
 * @author tobias
 */
public class Bullet extends GameObject {

    private int tankID;

    private int lifeTime = 180; //ca 3 sekunden
    private int lifeCounter = 0;

    protected int bulletType;

    public Bullet(double x, double y, int id, float velocity, float rotation, int tankID) {
        super(x, y, 10,25,id, velocity, rotation);
        setType(BULLET);
        this.tankID = tankID;
    }

    public void draw(PApplet p) {
        p.rectMode(p.CENTER);
        p.stroke(255, 0, 0, 255);
        p.pushMatrix();
        p.translate((int) getX(), (int) getY());
        p.translate((int) getWidth() / 2, (int) getHeight() / 2);
        p.rotate(p.radians((int) getRotation()));
        p.translate((int) -getWidth() / 2, (int) -getHeight() / 2);
        p.rect(0, 0, (int) getWidth(), (int) getHeight());
        p.popMatrix();
    }

    @Override
    public void tick() {
        if (lifeCounter < lifeTime) {
            lifeCounter++;
        } else {
            core.removeBullet(netID);
        }
    }

    public int getTankID() {
        return tankID;
    }

    @Override
    public void collide(GameObject otherObject) {
        if (otherObject.getType() == WALL) {
            if (((Wall) otherObject).getTickRotation() == 0) {
                for (int j = 0; j < 3; j++) {
                    core.getNetInterface().sendNewObject(new Explosion(x, y, 0));
                }
                core.removeBullet(netID);
            }
        }
//        if (otherObject.getType() == TANK) {
//            Tank t = (Tank) otherObject;
//            if(core.)
//        }
        if (otherObject.getType() == TANK) {
            if (!core.isPlayer(otherObject.getNetworkID())) {
                core.getNetInterface().sendNewObject(new Explosion(x, y, 0));
                Tank t = core.getTank(otherObject.getNetworkID());
                if (t != null) {
                    t.doDamage(0.5);
                }
                core.removeBullet(netID);
            }
        }
    }

    @Override
    public void parseObject(ObjectParser oP) {
        oP.write("" + type);
        oP.write("" + tankID);
        oP.write("" + (int) x);
        oP.write("" + (int) y);
        oP.write("" + (int) rotation);
        oP.write("" + netID);
    }

}
