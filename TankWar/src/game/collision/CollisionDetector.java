/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.collision;

import game.geom.Vector2D;
import game.objects.GameObject;
import java.util.ArrayList;

/**
 *
 * @author Tobias
 */
public class CollisionDetector {
    
    private Vector2D yAxis = new Vector2D(0, 1);

    public void checkCollision(ArrayList<GameObject> objs) {
        for (int i = 0; i < objs.size(); i++) {
            GameObject obj1 = objs.get(i);
            for (int j = 0; j < objs.size(); j++) {
                GameObject obj2 = objs.get(j);
                if(obj1!=obj2) {
                    if(collide(obj1, obj2)) {
                        obj1.collide(obj2);
                        obj2.collide(obj1);
                    }
                }
            }
        }
    }
    
    public boolean collide(GameObject o1, GameObject o2) {
        for (int i = 0; i < o1.getVectors().length; i++) {
            Vector2D axis = o1.getNorm(i).unitVector();
            Vector2D minMax1 = getMinMax(o1.getVectors(), axis);
            Vector2D minMax2 = getMinMax(o2.getVectors(), axis);
            if (minMax1.getY() < minMax2.getX() || minMax2.getY() < minMax1.getX()) {
                return false;
            }
        }
        for (int i = 0; i < o2.getVectors().length; i++) {
            Vector2D axis = o2.getNorm(i).unitVector();
            Vector2D minMax1 = getMinMax(o1.getVectors(), axis);
            Vector2D minMax2 = getMinMax(o2.getVectors(), axis);
            if (minMax1.getY() < minMax2.getX() || minMax2.getY() < minMax1.getX()) {
                return false;
            }
        }
        return true;
    }

    private Vector2D getMinMax(Vector2D[] vecs, Vector2D axis) {
        double min = vecs[0].dotProduct(axis), max = vecs[0].dotProduct(axis);
        for (int i = 1; i < vecs.length; i++) {
            double cLength = vecs[i].dotProduct(axis);
            if (cLength > max) {
                max = cLength;
            }
            if (cLength < min) {
                min = cLength;
            }
        }
        return new Vector2D(min, max);
    }
}
