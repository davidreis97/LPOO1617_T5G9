package com.drfl.twinstickshooter.server;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;

/**
 * Wrapper for kryonet server. Handles android controller packets received.
 */
public class TSSServer {

    /**
     * Debug flag.
     */
    private static final boolean DEBUG = false;

    /**
     * Port used for connections.
     */
    private static final int PORT = 54555;

    /**
     * Kryonet server.
     */
    private Server server;

    /**
     * Vector representing movement vector from android controller packet.
     */
    private static Vector2 movement = new Vector2(0, 0);

    /**
     * Vector representing shoot vector from android controller packet.
     */
    private static Vector2 shooting = new Vector2(0, 0);

    /**
     * Constructs a kryonet server and registers a listener so it receives packets.
     */
    public TSSServer() {

        server = new Server();
        registerPackets();

        server.addListener(new NetworkListener());

        try {
            server.bind(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        server.start();
    }

    /**
     * Initializes kryo instance so it handles android controller packets.
     */
    private void registerPackets() {

        Kryo kryo = server.getKryo();
        kryo.register(Packet.ControllerInfoPacket.class);
        kryo.register(Vector2.class);
    }

    /**
     * Sets server movement and shoot vectors received from android controller.
     *
     * @param cip The android controller packet
     */
    static void processControllerInfo(Packet.ControllerInfoPacket cip) {

        TSSServer.movement = cip.movement;
        TSSServer.shooting = cip.shooting;
        if(DEBUG) Log.info("Received Acceleration: " + cip.movement + " / Received Bullet: " + cip.shooting);
    }

    /**
     * Disposes of server resources.
     */
    public void dispose() {

        server.stop();

        try {
            this.server.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return Vector2 A vector copy representing movement
     */
    public static Vector2 getMovement() {
        return movement.cpy();
    }

    /**
     * @return Vector2 A vector copy representing direction of shooting
     */
    public static Vector2 getShooting() {
        return shooting.cpy();
    }

    /**
     * @return Array of connections to the server
     */
    public Connection[] getConnections() {
        return server.getConnections();
    }
}