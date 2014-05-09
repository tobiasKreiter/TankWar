/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.geom;

import java.awt.Point;

/**
 *
 * @author Tobias
 */
public class ExactPoint {

    private double x;
    private double y;

    public ExactPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Point toPoint() {
        return new Point((int) x, (int) y);
    }

}
