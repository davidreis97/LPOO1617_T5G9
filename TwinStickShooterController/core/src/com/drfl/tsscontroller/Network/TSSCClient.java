package com.drfl.tsscontroller.Network;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;

import java.io.IOException;

public class TSSCClient {

    public Client client;
    private int timeout;
    private int port;
    private String IPAddress;

    private boolean connected;
    private String errorMsg;

    public TSSCClient(String IPAddress) {

        client = new Client();
        timeout = 5000;

        errorMsg = "";

        this.port = 54555;
        this.IPAddress = IPAddress;

        registerPackets();

        client.addListener(new NetworkListener());

        try {
            client.start();
            client.connect(timeout, IPAddress, port);
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
            connected = false;
            errorMsg = e.getMessage();
        }
    }

    private void registerPackets(){
        Kryo kryo = client.getKryo();
        kryo.register(Packet.ControllerInfoPacket.class);
        kryo.register(Vector2.class);
    }

    public boolean isConnected() {
        return connected;
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

    public String getErrorMsg() {
        return errorMsg;
    }
}
