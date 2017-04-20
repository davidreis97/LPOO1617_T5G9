package com.drfl.twinstickshooter;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;

/**
 * Created by davidreis on 13/04/2017.
 */

public class TSSServer {
    private int port = 54555;

    private Server server;

    public TSSServer(){
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

    private void registerPackets(){
        Kryo kyro = server.getKryo();
        kyro.register(Packet.ControllerInfoPacket.class);
        kyro.register(Vector2.class);
    }

    public static void processControllerInfo(Packet.ControllerInfoPacket cip){
        TSSGame.setAcceleration(cip.acceleration);
        TSSGame.setBullets(cip.bullet);
        Log.info("Received Acceleration: " + cip.acceleration + " / Received Bullet: " + cip.bullet);
    }
}
