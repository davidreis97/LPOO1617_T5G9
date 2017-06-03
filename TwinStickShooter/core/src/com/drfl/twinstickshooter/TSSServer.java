package com.drfl.twinstickshooter;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;

//TODO make logging optional

public class TSSServer {
    private int port = 54555;

    private Server server;

    private static Vector2 movement = new Vector2(0, 0);
    private static Vector2 shooting = new Vector2(0, 0);

    public TSSServer() {

        server = new Server();
        registerPackets();

        server.addListener(new NetworkListener());

        try {
            server.bind(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        server.start();
    }

    private void registerPackets() {

        Kryo kryo = server.getKryo();
        kryo.register(Packet.ControllerInfoPacket.class);
        kryo.register(Vector2.class);
    }

    public static void processControllerInfo(Packet.ControllerInfoPacket cip) {

        TSSServer.movement = cip.movement;
        TSSServer.shooting = cip.shooting;
        Log.info("Received Acceleration: " + cip.movement + " / Received Bullet: " + cip.shooting);
    }

    /**
     * @return Vector2 representing player movement.
     */
    public static Vector2 getMovement() {
        return movement;
    }

    /**
     * @return Vector2 representing direction of shooting.
     */
    public static Vector2 getShooting() {
        return shooting;
    }
}
