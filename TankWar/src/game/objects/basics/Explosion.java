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
public class Explosion extends GameObject {

    private double radius = 0.1;
    private double i;

    public Explosion(double x, double y,int id) {
        super(x, y, 10, 10, 0, 0);
        setType(EXPLOSION);
    }

    @Override
    public void draw(PApplet p) {
        p.stroke(255, 138, 0, 255);
        p.strokeWeight(2);
        p.fill(0, 0, 0, 0);
        p.ellipseMode(PApplet.CENTER);
        p.ellipse((int) x, (int) y, (int) radius, (int) radius);
        p.fill(255, 255, 255, 255);
        p.strokeWeight(1);
    }

    @Override
    public void tick() {
        radius += i;
        if (radius >= 100) {
            core.removeExplosion(netID);
        }
        i += 0.6;
    }

    @Override
    public void collide(GameObject otherObject) {
    }

    @Override
    public void parseObject(ObjectParser oP) {
        oP.write("" + type);
        oP.write("" + netID);
        oP.write("" + (int)x);
        oP.write("" + (int)y);
    }

}
