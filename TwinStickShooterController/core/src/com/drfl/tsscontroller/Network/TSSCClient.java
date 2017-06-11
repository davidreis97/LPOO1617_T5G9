package com.drfl.tsscontroller.Network;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;

import java.io.IOException;

/**
 * Wrapper for kryonet client. Handles android controller packets to send.
 */
public class TSSCClient {

    /**
     * Milliseconds to attempt connection for.
     */
    private static final int TIMEOUT = 5000;

    /**
     * TCP port to use.
     */
    private static final int PORT = 54555;

    /**
     * Kryonet client.
     */
    public Client client;

    /**
     * Is connection established?
     */
    private boolean connected;

    /**
     * Error message if connection failed.
     */
    private String errorMsg = "";

    /**
     * Constructs a kryonet client and attempts to connect to a game server.
     *
     * @param IPAddress The IP address to use for the connection
     */
    public TSSCClient(String IPAddress) {

        client = new Client();

        registerPackets();

        client.addListener(new NetworkListener());

        try {
            client.start();
            client.connect(TIMEOUT, IPAddress, PORT);
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
            connected = false;
            errorMsg = e.getMessage();
        }
    }

    /**
     * Initializes kryo instance so it handles android controller packets.
     */
    private void registerPackets() {

        Kryo kryo = client.getKryo();
        kryo.register(Packet.ControllerInfoPacket.class);
        kryo.register(Vector2.class);
    }

    /**
     * @return Whether a connection was established
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     *  @return The current error message
     */
    public String getErrorMsg() {
        return errorMsg;
    }
}