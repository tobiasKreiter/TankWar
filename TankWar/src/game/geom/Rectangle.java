/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.geom;

import java.awt.Point;
import java.io.Serializable;

/**
 *
 * @author Tobias
 */
public class Rectangle {

    private double x;
    private double y;
    private double width;
    private double height;

    public Rectangle() {
    }

    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public Rectangle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return (int)x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return (int)y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return (int)width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return (int)height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean contains(int x, int y) {
        return getX() < x && x < getX() + getWidth()
                && getY() < y && y < getY() + getHeight();
    }

    public void setRect(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public void add(Point p) {
        add(p.x, p.y);
    }

    public void add(int x, int y) {
        double x1 = Math.min(getX(), x);
        double x2 = Math.max(getX() + getWidth(), x);
        double y1 = Math.min(getY(), y);
        double y2 = Math.max(getY() + getHeight(), y);
        setRect((int) x1, (int) y1, (int) (x2 - x1), (int) (y2 - y1));
    }

}
