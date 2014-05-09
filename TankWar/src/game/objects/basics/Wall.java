/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.objects.basics;

import game.objects.GameObject;
import java.awt.Color;
import java.util.Random;
import network.ObjectParser;
import processing.core.PApplet;

/**
 *
 * @author tobias
 */
public class Wall extends GameObject {

    private double tickRotation;

    public Wall(int x, int y, int width, int height, int id,float rotation, double tickRotation) {
        super(x, y, width, height, id, 0, rotation, 0);
        setType(WALL);
        this.tickRotation = tickRotation;
    }

    public void draw(PApplet p) {
        p.stroke(borderColor.getRGB());
        p.strokeWeight(2);
        p.rectMode(p.CORNER);
        p.pushMatrix();
        p.translate((int) x, (int) y);
        p.translate((int) width / 2, (int) height / 2);
        p.rotate(p.radians((int) rotation));
        p.translate((int) -width / 2, (int) -height / 2);
        p.rect(0, 0, (int) width, (int) height);
        p.popMatrix();
        p.strokeWeight(1);
    }

    @Override
    public void tick() {
        rotation += tickRotation;
    }
    
    public double getTickRotation() {
        return tickRotation;
    }

    @Override
    public void collide(GameObject otherObject) {
        if (otherObject.getType() == TANK) {
            Random r = new Random();
            borderColor = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255),255);
            Tank t = (Tank) otherObject;
            if (tickRotation==0) {
                t.bumpStop();
            } else {
                t.slowDown();
            }
        }
    }

    @Override
    public void parseObject(ObjectParser oP) {
        
    }

}
