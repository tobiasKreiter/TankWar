/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import game.core.Core;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import network.packages.PingPackage;

/**
 *
 * @author tobias
 */
public class Host {

    private NetInterface hState;

    private String type = "Server";

    public Host(Core core) {
        initServer();
        hState.setCore(core);
    }

    public void initServer() {
        InetAddress clientAdd = findBroadcastAddress();
        if (clientAdd == null) {
            try {
                System.out.println("Could not find Broadcast ip");
                System.out.println("Please enter the broadcast or server ip:");
                Scanner s = new Scanner(System.in);
                clientAdd = InetAddress.getByName(s.nextLine());
                s.close();
            } catch (UnknownHostException ex) {
                Logger.getLogger(Host.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println(clientAdd.getHostAddress());
        if (!couldContactServer(clientAdd)) {
            try {
                System.out.println("Enter server address or press enter to create a server: ");
                Scanner s = new Scanner(System.in);
                clientAdd = InetAddress.getByName(s.nextLine());
                s.close();
            } catch (UnknownHostException ex) {
                Logger.getLogger(Host.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (!couldContactServer(clientAdd)) {
                try {
                    hState = new Server(new DatagramSocket(9876));
                } catch (SocketException ex) {
                    Logger.getLogger(Host.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    hState = new Client(new DatagramSocket(), clientAdd);
                    type = "Client";
                } catch (SocketException ex) {
                    Logger.getLogger(Host.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            try {
                hState = new Client(new DatagramSocket(), clientAdd);
                type = "Client";
            } catch (SocketException ex) {
                Logger.getLogger(Host.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private InetAddress findBroadcastAddress() {
        try {
            InetAddress address = Inet4Address.getLocalHost();
            String ip = address.getHostAddress();
            String[] split = ip.split("\\.");
            if (split[0].equals("127")) {
                return null;
            }
            split[split.length - 1] = "255";
            String broadcast = split[0];
            for (int i = 1; i < split.length; i++) {
                broadcast += "." + split[i];
            }
            return InetAddress.getByName(broadcast);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Host.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public NetInterface getNetworkInterface() {
        return hState;
    }

    private boolean couldContactServer(InetAddress address) {
        DatagramSocket socket = null;
        byte[] buffer = new byte[1024];
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(1000);
            byte[] data = compress(new PingPackage().getData().getBytes());
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, address, 9876);
            socket.send(sendPacket);
            DatagramPacket dPacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(dPacket);
            socket.close();
            return true;
        } catch (SocketTimeoutException ste) {
            System.out.println("no server found");
        } catch (IOException ex) {
            Logger.getLogger(Host.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private byte[] compress(byte[] data) throws IOException {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        deflater.finish();
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer); // returns the generated code... index  
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

    public String getType() {
        return type;
    }

}
