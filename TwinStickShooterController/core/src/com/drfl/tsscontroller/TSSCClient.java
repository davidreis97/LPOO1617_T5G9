package com.drfl.tsscontroller;

/**
 * Created by davidreis on 18/04/2017.
 */

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;

import java.io.IOException;
import java.util.Scanner;

public class TSSCClient {

    public Client client;
    private int timeout;
    private int port;
    private String IPAddress;

    public TSSCClient(){
        client = new Client();
        timeout = 5000;
        port = 54555;
        IPAddress = "192.168.1.3";
        registerPackets();

        client.addListener(new NetworkListener());
        try {
            client.start();
            client.connect(timeout,IPAddress,port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerPackets(){
        Kryo kryo = client.getKryo();
        kryo.register(Packet.ControllerInfoPacket.class);
        kryo.register(Vector2.class);
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }
}
