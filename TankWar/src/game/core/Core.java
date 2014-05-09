/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core;

import game.collision.CollisionDetector;
import game.objects.GameObject;
import game.objects.basics.Bullet;
import game.objects.basics.Explosion;
import game.objects.basics.Tank;
import game.objects.basics.Wall;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import network.Host;
import network.NetInterface;
import processing.core.PApplet;

/**
 *
 * @author Tobi
 */
public class Core {

    private ConcurrentSkipListMap<Integer, Tank> tanks = new ConcurrentSkipListMap();
    private ConcurrentSkipListMap<Integer, Bullet> bullets = new ConcurrentSkipListMap();
    private ConcurrentSkipListMap<Integer, Explosion> explosions = new ConcurrentSkipListMap();

    private ConcurrentSkipListMap<Integer, Wall> walls = new ConcurrentSkipListMap();

    private ConcurrentSkipListMap<Integer, GameObject> allObjects = new ConcurrentSkipListMap();

    private Tank player;

    public static int playerID;

    private Host host;
    private NetInterface nInf;

    private CollisionDetector colDec = new CollisionDetector();

    public Core() {
        initNetwork();
        initPlayer();
        initMap();
    }

    private void initNetwork() {
        host = new Host(this);
        nInf = host.getNetworkInterface();
    }

    public void tick() {
        colDec.checkCollision(new ArrayList(allObjects.values()));
        nInf.tick();
    }

    public synchronized void draw(PApplet p) {
        p.smooth();
        player.draw(p);
        for (Map.Entry<Integer, Wall> entry : walls.entrySet()) {
            entry.getValue().draw(p);
        }
        for (Map.Entry<Integer, Tank> entry : tanks.entrySet()) {
            entry.getValue().draw(p);
        }
        for (Map.Entry<Integer, Bullet> entry : bullets.entrySet()) {
            entry.getValue().draw(p);
        }
        for (Map.Entry<Integer, Explosion> entry : explosions.entrySet()) {
            entry.getValue().draw(p);
        }
        drawPlayerInfo(p);
        if (player.isDead()) {
            p.textSize(40);
            Rectangle2D rec = p.getGraphics().getFontMetrics().getStringBounds("Du bist tot!", p.getGraphics());
            p.text("Du bist tot!", (p.getWidth() / 2), p.getHeight() / 2 + (int) rec.getHeight() / 2);
        }
        p.textSize(12);
        p.text("Ping: " + nInf.getPing(), 10, 130);
        p.text("Packages Lost: " + nInf.getLostCounter() + " (" + nInf.getLostInPercent() + "%)", 10, 150);
        p.text("Packages Send: " + nInf.getSendetCounter(), 10, 170);
        p.text("Packages Received: " + nInf.getReceiveCounter(), 10, 190);
        p.text("Enemys: " + tanks.size(), 10, 50);
        p.text("Bullets: " + bullets.size(), 10, 70);
        p.text("Explosions: " + explosions.size(), 10, 90);
        p.text("Walls: " + walls.size(), 10, 110);
    }

    private void drawPlayerInfo(PApplet p) {
        p.text("Ammo: " + player.getAmmo(), 10, p.getHeight() - 30);
        p.rectMode(p.CORNER);
        p.stroke(0, 0, 0, 0);
        p.fill(255, 0, 0, 255);
        p.rect(p.getWidth() - 10, p.getHeight() - 20, -(int) (100 - player.getHealth()) * 2, 15);
        p.fill(0, 255, 0, 255);
        p.rect(p.getWidth() - 210, p.getHeight() - 20, (int) player.getHealth() * 2, 15);
        p.fill(255, 255, 255, 255);
    }

    public ConcurrentSkipListMap<Integer, Tank> getTanks() {
        return tanks;
    }

    public ConcurrentSkipListMap<Integer, GameObject> getTankExceptOne(int id) {
        ConcurrentSkipListMap<Integer, Tank> allTanks = new ConcurrentSkipListMap();
        allTanks.putAll(tanks);
        allTanks.put(playerID, player);
        allTanks.remove(id);
        ConcurrentSkipListMap<Integer, GameObject> update = (ConcurrentSkipListMap) allTanks;
        return update;
    }

    public Tank getTank(int id) {
        if (id != playerID) {
            return tanks.get(id);
        } else {
            return player;
        }
    }

    public ConcurrentSkipListMap<Integer, Bullet> getBullets() {
        return bullets;
    }

    public ConcurrentSkipListMap<Integer, Explosion> getExplosions() {
        return explosions;
    }

    public ConcurrentSkipListMap<Integer, GameObject> getAllObjects() {
        return allObjects;
    }

    public Tank getPlayer() {
        return player;
    }

    public NetInterface getNetInterface() {
        return nInf;
    }

    public Host getHost() {
        return host;
    }

    public void tickPlayer() {
        player.tickProxy();
    }

    public void tickWalls() {
        for (Map.Entry<Integer, Wall> wall : walls.entrySet()) {
            if (wall != null) {
                wall.getValue().tickProxy();
            }
        }
    }

    public void tickEnemys() {
        for (Map.Entry<Integer, Tank> tank : tanks.entrySet()) {
            if (tank != null) {
                tank.getValue().tickProxy();
            }
        }
    }

    public void tickBullets() {
        for (Map.Entry<Integer, Bullet> bullet : bullets.entrySet()) {
            if (bullet != null) {
                bullet.getValue().tickProxy();
            }
        }
    }

    public void tickExplosions() {
        for (Map.Entry<Integer, Explosion> explosion : explosions.entrySet()) {
            if (explosion != null) {
                explosion.getValue().tickProxy();
            }
        }
    }

    public void addTank(Tank t) {
        t.setCore(this);
        allObjects.put(t.getNetworkID(), t);
        tanks.put(t.getNetworkID(), t);
    }

    public void addBullet(Bullet b) {
        b.setCore(this);
        allObjects.put(b.getNetworkID(), b);
        bullets.put(b.getNetworkID(), b);
    }

    public void addBullet(int id, Bullet b) {
        b.setNetworkID(id);
        b.setCore(this);
        bullets.put(id, b);
        allObjects.put(id, b);
    }

    public void addExplosion(Explosion ex) {
        ex.setCore(this);
        explosions.put(ex.getNetworkID(), ex);
        allObjects.put(ex.getNetworkID(), ex);
    }

    public void addExplosion(int id, Explosion ex) {
        ex.setNetworkID(id);
        ex.setCore(this);
        explosions.put(id, ex);
    }

    public void removeTank(int id) {
        allObjects.remove(id);
        tanks.remove(id);
    }

    public void removeBullet(int id) {
        allObjects.remove(id);
        bullets.remove(id);
    }

    public void removeExplosion(int id) {
        allObjects.remove(id);
        explosions.remove(id);
    }

    public ConcurrentSkipListMap<Integer, GameObject> differObjects(int netVersion, int type) {
        ConcurrentSkipListMap<Integer, GameObject> newObjects = new ConcurrentSkipListMap();
        ConcurrentSkipListMap<Integer, GameObject> sendObjects = new ConcurrentSkipListMap();
        switch (type) {
            case GameObject.TANK:
                newObjects.putAll(tanks);
                break;
            case GameObject.BULLET:
                newObjects.putAll(bullets);
                break;
            case GameObject.EXPLOSION:
                newObjects.putAll(explosions);
                break;
        }
        for (Map.Entry<Integer, GameObject> entry : newObjects.entrySet()) {
            if (entry.getKey() >= netVersion) {
                sendObjects.put(entry.getKey(), entry.getValue());
            }
        }
        System.out.println("differ");
        if (type == GameObject.BULLET) {
            System.out.println("bullet");
            System.out.println(sendObjects.size());
        }
        return sendObjects;
    }

    public Set<Integer> getTankSet() {
        return tanks.keySet();
    }

    public Set<Integer> getPlayerSet() {
        HashSet<Integer> i = new HashSet();
        i.add(playerID);
        return i;
    }

    public Set<Integer> getBulletSet() {
        return bullets.keySet();
    }

    public Set<Integer> getExplosionSet() {
        return explosions.keySet();
    }

    public boolean isPlayer(int id) {
        return id == playerID;
    }

    private void initPlayer() {
        nInf.start();
        int newID = nInf.getNewID();
        player = new Tank(new Random().nextInt(1000), new Random().nextInt(300), 0, newID, 0);
        player.setCore(this);
        playerID = newID;
        allObjects.put(newID, player);
    }

    private void initMap() {
        int j = 0;
        for (int i = 0; i < 2; i++) {
            int rot = 45 + (j * 90);
            Wall w = new Wall(250, 100 + (i * 500), 250, 30, 0, rot, 0);
            walls.put(nInf.getNewID(), w);
            allObjects.put(allObjects.size() + 1, w);
            rot += 90;
            Wall w2 = new Wall(800, 100 + (i * 500), 250, 30, 0, rot, 0);
            walls.put(nInf.getNewID(), w2);
            allObjects.put(allObjects.size() + 1, w2);
            j++;
        }
    }
}
