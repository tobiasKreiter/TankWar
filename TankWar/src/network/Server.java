/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import game.objects.GameObject;
import game.objects.basics.Bullet;
import game.objects.basics.Explosion;
import game.objects.basics.Tank;
import network.packages.DataPackage;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import network.packages.LoginPackage;
import network.packages.NewObjectPackage;
import network.packages.PingPackage;

/**
 *
 * @author tobias
 */
public class Server extends NetInterface {

    private ExecutorService pool = Executors.newCachedThreadPool();

    public Server(DatagramSocket s) {
        super(s);
        server = true;
        try {
            s.setSoTimeout(200);
        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        while (true) {
            final DataPackage dPackage = receive();
            if (dPackage != null) {
                send(doPackageAction(dPackage));
            }
            try {
                Thread.sleep(1);
            } catch (Exception e) {
            }
        }
    }

    private DataPackage doPackageAction(DataPackage dPackage) {
        switch (dPackage.getType()) {
            case DataPackage.PING:
                return new PingPackage();
            case DataPackage.NEW_ID:
                return new LoginPackage(getNewID());
            case DataPackage.NEW_OBJECT_REQUEST:
                identificateAndUpdateObject(dPackage.getGameObject());
                return new PingPackage();
            case DataPackage.NEW_UPDATE_REQUEST:
                switch (dPackage.getUpdateType()) {
                    case GameObject.BULLET:
                        return new NewObjectPackage(networkVersion, core.differObjects(dPackage.getNetworkVersion(), dPackage.getUpdateType()));
                    case GameObject.EXPLOSION:
                        return new NewObjectPackage(networkVersion, core.differObjects(dPackage.getNetworkVersion(), dPackage.getUpdateType()));
                    default: //Tanks
                        return new NewObjectPackage(networkVersion, (ConcurrentSkipListMap) core.getTanks());
                }
        }
        return null;
    }

    @Override
    public void tick() {
        core.tickPlayer();
        //core.tickEnemys();
        core.tickBullets();
        core.tickWalls();
        core.tickExplosions();
    }

    @Override
    public int getNewID() {
        networkVersion++;
        return networkVersion;
    }

    @Override
    public void getNewObjects() {
    }

    @Override
    public void sendNewObject(GameObject obj) {
        identificateAndUpdateObject(obj);
    }

    protected void identificateAndUpdateObject(GameObject obj) {
        if (obj != null) {
            switch (obj.getType()) {
                case GameObject.TANK:
                    Tank t = (Tank) obj;
                    core.addTank(t);
                    break;
                case GameObject.BULLET:
                    Bullet b = (Bullet) obj;
                    core.addBullet(getNewID(), b);
                    break;
                case GameObject.EXPLOSION:
                    Explosion e = (Explosion) obj;
                    core.addExplosion(getNewID(), e);
                    break;
            }
        }
    }

}
