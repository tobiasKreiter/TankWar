/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network.packages;

import game.core.Core;
import game.objects.GameObject;
import game.objects.basics.Bullet;
import game.objects.basics.Explosion;
import game.objects.basics.Tank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import network.ObjectParser;

/**
 *
 * @author tobias
 */
public class DataPackage {

    protected int type = 0;

    public static final int PING = 0;
    public static final int NEW_ID = 1;
    public static final int NEW_OBJECT_REQUEST = 2;
    public static final int NEW_UPDATE_REQUEST = 3;
    public static final int REMOVE = 4;

    protected int id;

    protected int updateType;

    protected int networkVersion = 0;

    protected ConcurrentSkipListMap<Integer, GameObject> objects = null;

    protected ConcurrentSkipListMap<Integer, Tank> tanks = null;

    protected ConcurrentSkipListMap<Integer, Bullet> bullets = null;

    protected ConcurrentSkipListMap<Integer, Explosion> explosions = null;

    protected ArrayList<GameObject> trash = null;

    protected String data = "";

    public DataPackage(int networkVersion) {
        this.networkVersion = networkVersion;
    }

    public DataPackage(String data) {
        this.data = data;
        convertBack();
    }

    protected void convertToString() {
        data += networkVersion + "/";
        data += type + "/";
        data += id + "/";
        data += updateType + "/";
        if (objects != null) {
            for (Map.Entry<Integer, GameObject> entry : objects.entrySet()) {
                if (entry != null) {
                    ObjectParser oP = new ObjectParser();
                    entry.getValue().parseObject(oP);
                    data += oP.getValue() + "/";
                }
            }
        }
    }

    protected void convertBack() {
        String[] split = data.split("/");
        try {
            networkVersion = Integer.parseInt(split[0]);
            type = Integer.parseInt(split[1]);
            id = Integer.parseInt(split[2]);
            updateType = Integer.parseInt(split[3]);
        } catch (Exception e) {
        }
        int index = 4;
        objects = new ConcurrentSkipListMap();
        for (int i = index; i < split.length; i++) {
            String[] values = split[i].split(";");
            int type = Integer.parseInt(values[0]);
            if (type == GameObject.TANK) {
                Tank t = toTank(values);
                if (t != null) {
                    if (t.getNetworkID() != Core.playerID) {
                        objects.put(t.getNetworkID(), t);
                    }
                }
            } else if (type == GameObject.BULLET) {
                Bullet b = toBullet(values);
                objects.put(b.getNetworkID(), b);
            } else if (type == GameObject.EXPLOSION) {
                Explosion e = toExplosion(values);
                objects.put(e.getNetworkID(), e);
            }
        }
    }

    private Tank toTank(String[] values) {
        try {
            int type = Integer.parseInt(values[0]);
            int id = Integer.parseInt(values[1]);
            int team = Integer.parseInt(values[2]);
            int rotation = Integer.parseInt(values[3]);
            int x = Integer.parseInt(values[4]);
            int y = Integer.parseInt(values[5]);
            float vel = Float.parseFloat(values[6]);
            Tank t;
            t = new Tank(x, y, team, id, vel);
            t.setRotation(rotation);
            return t;
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private Bullet toBullet(String[] values) {
        int tankID = Integer.parseInt(values[1]);
        int x = Integer.parseInt(values[2]);
        int y = Integer.parseInt(values[3]);
        int rot = Integer.parseInt(values[4]);
        int id = Integer.parseInt(values[5]);
        return new Bullet(x, y, id, 0, rot, tankID);
    }

    private Explosion toExplosion(String[] values) {
        int id = Integer.parseInt(values[1]);
        int x = Integer.parseInt(values[2]);
        int y = Integer.parseInt(values[3]);
        return new Explosion(x, y, id);
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public int getNetworkVersion() {
        return networkVersion;
    }

    public GameObject getGameObject() {
        return objects.entrySet().iterator().next().getValue();
    }

    public ConcurrentSkipListMap<Integer, Tank> getTanks() {
        return tanks;
    }

    public ConcurrentSkipListMap<Integer, GameObject> getObjects() {
        return objects;
    }

    public ConcurrentSkipListMap<Integer, Bullet> getBullets() {
        return bullets;
    }

    public int getUpdateType() {
        return updateType;
    }

    public ArrayList<GameObject> getTrash() {
        return trash;
    }

    public String getData() {
        return data;
    }

}
