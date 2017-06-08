package com.drfl.twinstickshooter.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

/**
 * Handles connection events, forwarding packets to TSSServer.
 */
public class NetworkListener extends Listener {

    //NOTEME javadoc
    /**
     * Debug flag.
     */
    private static final boolean DEBUG = false;

    //NOTEME javadoc
    @Override
    public void connected(Connection conn) {
        Log.info("[SERVER] Someone connected.");
    }

    //NOTEME javadoc
    @Override
    public void disconnected(Connection conn) {
        Log.info("[SERVER] Someone disconnected.");
    }

    //NOTEME javadoc
    @Override
    public void received(Connection conn, Object obj) {

        if (obj instanceof Packet.ControllerInfoPacket) {
            if(DEBUG) Log.info("[SERVER] ControllerInfoPacket received.");
            TSSServer.processControllerInfo((Packet.ControllerInfoPacket) obj);

        } else {
            if(DEBUG) Log.info("[SERVER] Unknown packet received.");
        }
    }
}