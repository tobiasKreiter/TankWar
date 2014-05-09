/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;


import game.core.Core;
import game.objects.GameObject;
import game.objects.basics.Bullet;
import game.objects.basics.Explosion;
import game.objects.basics.Tank;
import java.io.ByteArrayOutputStream;
import network.packages.DataPackage;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 *
 * @author tobias
 */
public abstract class NetInterface extends Thread {

    protected int networkVersion = 0;
    
    protected DatagramSocket socket;

    protected byte[] buffer = new byte[1024 * 4];

    protected InetAddress address;

    protected int port;

    protected Core core;

    protected boolean server = false;

    protected int ping;

    private long pingMes = 0;

    private int lostCounter = 1;

    private int sendCounter = 1;

    private int receiveCounter = 1;

    public NetInterface(DatagramSocket s) {
        this.socket = s;
        try {
            this.socket.setSoTimeout(70);
        } catch (SocketException ex) {
            Logger.getLogger(NetInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public DataPackage receive() {
        try {
            DatagramPacket dPacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(dPacket);
            receiveCounter++;
            address = dPacket.getAddress();
            port = dPacket.getPort();
            String packet = new String(decompress(dPacket.getData()));
            DataPackage p = new DataPackage(packet);
            ping = (int) (System.currentTimeMillis() - pingMes);
            return p;
        } catch (SocketTimeoutException stE) {
            lostCounter++;
        } catch (DataFormatException ex) {
            Logger.getLogger(NetInterface.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NetInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void send(DataPackage dPacket) {
        try {
            sendCounter++;
            pingMes = System.currentTimeMillis();
            byte[] data = compress(dPacket.getData().getBytes());
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, address, port);
            socket.send(sendPacket);
            buffer = new byte[Math.max(sendPacket.getLength(), buffer.length)];
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private byte[] compress(byte[] data) throws IOException {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        deflater.finish();
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        byte[] output = outputStream.toByteArray();
        deflater.end();
        return output;
    }

    private byte[] decompress(byte[] data) throws IOException, DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        byte[] output = outputStream.toByteArray();
        inflater.end();
        return output;
    }

    public void setCore(Core core) {
        this.core = core;
    }

    public boolean isServer() {
        return server;
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
                    core.addBullet(b);
                    break;
                case GameObject.EXPLOSION:
                    Explosion e = (Explosion) obj;
                    core.addExplosion(e);
                    break;
            }
        }
    }

    public int getPing() {
        return ping;
    }

    public int getLostCounter() {
        return lostCounter;
    }

    public int getSendetCounter() {
        return sendCounter;
    }

    public double getLostInPercent() {
        return ((double) lostCounter / sendCounter) * 100;
    }

    public int getReceiveCounter() {
        return receiveCounter;
    }

    public abstract void tick();

    public abstract int getNewID();

    public abstract void getNewObjects();

    public abstract void sendNewObject(GameObject obj);

    //public abstract void doDamageTo(int id, double damage);
}
