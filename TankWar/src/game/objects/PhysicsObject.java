/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.objects;

import game.geom.ExactPoint;
import game.geom.Rectangle;
import game.geom.Vector2D;
import java.awt.geom.Line2D;

/**
 *
 * @author Tobi
 */
public abstract class PhysicsObject extends NetworkObject {

    protected float rotation;

    protected float velocity;

    protected float maxVelocity;

    protected float minVelocity;

    protected float acceleration;

    protected float maxAcceleration;

    protected float minAcceleration;

    protected boolean accelerating = true;

    protected double damping = 0.99f; //default damping

    protected Rectangle collisionRectangle;

    protected Vector2D[] vectors = new Vector2D[4]; //Rectangle has 4 sides.
    protected Vector2D[] norms = new Vector2D[4];
    protected ExactPoint[] corners;
    protected Line2D[] edges = new Line2D[4];

    private int collisionCounter = 0; //for bug fixing

    public PhysicsObject(double x, double y, double width, double height, int id, float velocity) {
        super(x, y, width, height, id);
        this.velocity = velocity;
        updateVectors();
    }

    public PhysicsObject(double x, double y, double width, double height, int id, float velocity, float rotation) {
        this(x, y, width, height, id, velocity);
        this.rotation = rotation;
        updateVectors();
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void addRotation(float rotation) {
        this.rotation += rotation;
    }

    public float getVelocity() {
        return velocity;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public double getDamping() {
        return damping;
    }

    public float getMaxVelocity() {
        return maxVelocity;
    }

    public void setMaxVelocity(float maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    public float getMinVelocity() {
        return minVelocity;
    }

    public void setMinVelocity(float minVelocity) {
        this.minVelocity = minVelocity;
    }

    public float getMaxAcceleration() {
        return maxAcceleration;
    }

    public void setMaxAcceleration(float maxAcceleration) {
        this.maxAcceleration = maxAcceleration;
    }

    public float getMinAcceleration() {
        return minAcceleration;
    }

    public void setMinAcceleration(float minAcceleration) {
        this.minAcceleration = minAcceleration;
    }

    public boolean isAccelerating() {
        return accelerating;
    }

    public void setAccelerating(boolean accelerating) {
        this.accelerating = accelerating;
    }

    public void setDamping(double damping) {
        this.damping = damping;
    }

    public Rectangle getCollisionRectangle() {
        return collisionRectangle;
    }

    public Vector2D[] getVectors() {
        return vectors;
    }

    public Vector2D getNorm(int i) {
        return norms[i <= norms.length && i >= 0 ? i : 0];
    }

    public ExactPoint[] getCorners() {
        return corners;
    }

    public Line2D[] getEdges() {
        return edges;
    }

    public void updateVectors() {
        collisionRectangle = new Rectangle(x, y, width, height);
        corners = new ExactPoint[4];
        vectors = new Vector2D[4];
        edges = new Line2D[4];
        norms = new Vector2D[4];
        corners[0] = translatePoint(-(width / 2), -(height / 2));
        corners[1] = translatePoint((width / 2), -(height / 2));
        corners[2] = translatePoint((width / 2), (height / 2));
        corners[3] = translatePoint(-(width / 2), (height / 2));
        collisionRectangle.add(corners[0].toPoint());
        collisionRectangle.add(corners[1].toPoint());
        collisionRectangle.add(corners[2].toPoint());
        collisionRectangle.add(corners[3].toPoint());
        for (int j = 0; j < 4; j++) {
            vectors[j] = new Vector2D(corners[j].getX(), corners[j].getY());
        }
        for (int j = 0; j < 4; j++) {
            if (j < 3) {
                norms[j] = vectors[j + 1].minus(vectors[j]).normVector();
                edges[j] = new Line2D.Double(corners[j].toPoint(), corners[j + 1].toPoint());
            } else {
                norms[j] = vectors[0].minus(vectors[j]).normVector();
                edges[j] = new Line2D.Double(corners[j].toPoint(), corners[0].toPoint());
            }
        }
    }

    protected ExactPoint translatePoint(double x, double y) {
        double xx = ((x * Math.cos(getAngleInRadians()) - y * Math.sin(getAngleInRadians())));
        double yy = ((x * Math.sin(getAngleInRadians()) + y * Math.cos(getAngleInRadians())));
        xx += width / 2;
        yy += height / 2;
        return new ExactPoint(xx + this.x, yy + this.y);
    }

    public double getAngleInRadians() {
        return (rotation * (Math.PI / 180));
    }

    protected void move() {
        double aX = 0;
        double aY = 0;
        if (!accelerating) {
            acceleration = 0;
        }
        if (velocity < maxVelocity) {
            velocity += acceleration;
        } else {
            velocity = maxVelocity;
        }
        aX = Math.sin(rotation * (Math.PI / 180)) * velocity;
        aY = Math.cos(rotation * (Math.PI / 180)) * velocity * -1;
        x += aX;
        y += aY;
        if (velocity < 0.01 && velocity > 0 || velocity > -0.01 && velocity < 0) {
            velocity = 0;
        }
        velocity *= damping;
    }

    public void bumpStop() {
        if (collisionCounter == 0) {
            if (velocity > 0) {
                this.velocity = -Math.abs(velocity / 2);
            } else {
                this.velocity = Math.abs(velocity / 2);
            }
            collisionCounter++;
        } else {
            collisionCounter = 0;
        }
    }

    public void slowDown() {
        this.velocity /= 2;
    }

}
