/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import game.objects.GameObject;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import network.packages.DataPackage;
import network.packages.LoginPackage;
import network.packages.NewObjectPackage;
import network.packages.NewUpdatePackage;

/**
 *
 * @author tobias
 */
public class Client extends NetInterface {

    private int updateIntervall = 2;

    public Client(DatagramSocket s, InetAddress address) {
        super(s);
        this.address = address;
        this.port = 9876;
    }

    @Override
    public void run() {
        while (true) {
            sendNewObject(core.getPlayer()); //Update Player
            getNewObjects();
            try {
                Thread.sleep(25);
            } catch (Exception e) {
            }
        }
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
        DataPackage dP = null;
        do {
            send(new LoginPackage(0));
            dP = receive();
        } while (dP == null);
        return dP.getId();
    }

    @Override

    public void getNewObjects() {
        ConcurrentHashMap<Integer, GameObject> newObjects = new ConcurrentHashMap();
        send(new NewUpdatePackage(networkVersion, GameObject.TANK));
        DataPackage tanks = receive();
        if (tanks != null) {
            newObjects.putAll(tanks.getObjects());
        } else {
            System.out.println("no tanks");
        }
        send(new NewUpdatePackage(networkVersion, GameObject.BULLET));
        DataPackage bullets = receive();
        if (bullets != null) {
            newObjects.putAll(bullets.getObjects());
        } else {
            System.out.println("no bullets");
        }
        send(new NewUpdatePackage(networkVersion, GameObject.EXPLOSION));
        DataPackage exs = receive();
        if (exs != null) {
            this.networkVersion = exs.getNetworkVersion();
            newObjects.putAll(exs.getObjects());
        } else {
            System.out.println("no explosions");
        }
        for (Map.Entry<Integer, GameObject> entry : newObjects.entrySet()) {
            if (entry != null) {
                identificateAndUpdateObject(entry.getValue());
            }
        }
    }

    @Override
    public void sendNewObject(GameObject obj) {
        if (obj != null) {
            send(new NewObjectPackage(obj));
            receive();
        }
    }

//    @Override
//    public void removeObject(GameObject obj) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void doDamageTo(int id, double damage) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

}
