package com.drfl.twinstickshooter.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

/**
 * Handles connection events, forwarding packets to TSSServer.
 */
public class NetworkListener extends Listener {

    /**
     * Debug flag.
     */
    private static final boolean DEBUG = false;

    /**
     * Connection established.
     *
     * @param conn The TCP and optionally UDP connection between Client and Server that was established
     */
    @Override
    public void connected(Connection conn) {
        Log.info("[SERVER] Someone connected.");
    }

    /**
     * Connection ended.
     *
     * @param conn The TCP and optionally UDP connection between Client and Server that ended
     */
    @Override
    public void disconnected(Connection conn) {
        Log.info("[SERVER] Someone disconnected.");
    }

    /**
     * Received a packet. Updates the server with the current packet info for use in-game.
     *
     * @param conn The TCP and optionally UDP connection between Client and Server associated with the reception
     * @param obj The received object
     */
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