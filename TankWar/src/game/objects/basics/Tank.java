/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.objects.basics;

import game.objects.GameObject;
import java.awt.Color;
import java.awt.Point;
import java.util.Random;
import network.ObjectParser;
import processing.core.*;

/**
 *
 * @author tobias
 */
public class Tank extends GameObject {

    private double health = 100;

    private final int MAX_AMMO = 40;

    private int ammo = MAX_AMMO;

    private double rotTick = 0;

    private double spread = 5;

    private int fireCounter = 0;
    
    private boolean isDead = false;

    public Tank(double x, double y, int team, int id, float velocity) {
        super(x, y, 50, 100, id, velocity, team);
        setType(TANK);
        setDamping(0.92f);
        setMaxVelocity(8);
    }
    
    public int getAmmo() {
        return ammo;
    }

    public int getTeam() {
        return team;
    }

    public void setRotationIntervall(double rot) {
        this.rotTick = rot;
    }

    public double getRotationIntervall() {
        return rotTick;
    }
    
    public double getHealth() {
        return health;
    }

    public void tick() {
        rotation += rotTick;
    }

//    private void decelerate() {
//        if (vel > 0) {
//            vel -= velBuffer;
//        }
//        if (vel < 0) {
//            vel += velBuffer;
//        }
//        if (vel < velBuffer && vel > 0) {
//            vel = 0;
//        }
//        if (vel > -velBuffer && vel < 0) {
//            vel = 0;
//        }
//    }

    public void draw(PApplet p) {
        p.rectMode(p.CORNER);
        p.stroke(Color.BLUE.getRGB());
        p.pushMatrix();
        p.translate((int) getX(), (int) getY());
        p.translate((int) width / 2, (int) height / 2);
        p.rotate(p.radians((int) rotation));
        p.translate((int) -width / 2, (int) -height / 2);
        p.rect(0, 0, (int) width, (int) height);
        p.rect((int) width / 2 - 5, -40, 10, 40);
        p.rect(-10, 5, 10, (int) height - 10);
        p.rect((int) width, 5, 10, (int) height - 10);
        p.popMatrix();
        p.text("Tank 1", (int) corners[3].getX(), (int) corners[3].getY() + 20);
        p.text("Speed: "+velocity, 10, 300);
        p.text("Acceleration: "+acceleration, 10, 330);
        p.fill(255, 255, 255, 255);
    }

    public void doDamage(double damage) {
        if (health - damage > 0) {
            health -= damage;
        } else {
            health = 0; 
            isDead = true;
        }
    }
    
    public boolean isDead() {
        return isDead;
    }

    public void fire() {
        if (fireCounter < 40) {
            fireCounter++;
        } else {
            fireD();
            fireCounter = 0;
        }

    }

    public void fireD() {
        if (ammo > 0) {
            ammo--;
            Point p = translatePoint(0.5, -(height - 20)).toPoint();
            //core.getNetInterface().sendNewObject(new Bullet(p.getX(), p.getY(), 15, rotation - (new Random().nextInt((int) spread) - (spread / 2)), team, 0, 0)); //TODO networkID and tankID
            setAcceleration(-2);
        }
    }

    public void reload() {
        this.ammo = MAX_AMMO;
    }
    
    @Override
    public void collide(GameObject otherObject) {
        if(otherObject.getType()==TANK) {
            Tank t = (Tank) otherObject;
            t.bumpStop();
        }
    }

    @Override
    public void parseObject(ObjectParser oP) {
        oP.write("" + TANK);
        oP.write("" + netID);
        oP.write("" + team);
        oP.write("" + (int) rotation);
        oP.write("" + (int) x);
        oP.write("" + (int) y);
        oP.write("" + velocity);
        oP.close();
    }

}
