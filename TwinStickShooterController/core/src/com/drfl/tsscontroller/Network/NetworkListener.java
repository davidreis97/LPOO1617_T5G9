package com.drfl.tsscontroller.Network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

/**
 * Handles connection events.
 */
public class NetworkListener extends Listener {

    //NOTEME javadoc
    /**
     * Connection established.
     *
     * @param conn The TCP and optionally UDP connection between Client and Server that was established
     */
    @Override
    public void connected(Connection conn) {
        Log.info("[CLIENT] You have connected.");
    }

    //NOTEME javadoc
    /**
     * Connection ended.
     *
     * @param conn The TCP and optionally UDP connection between Client and Server that ended
     */
    @Override
    public void disconnected (Connection conn) {
        Log.info("[CLIENT] You have disconnected.");
    }

    //NOTEME javadoc
    /**
     * Received a packet.
     *
     * @param conn The TCP and optionally UDP connection between Client and Server the packet originated from
     * @param obj The received object
     */
    @Override
    public void received (Connection conn, Object obj) {
        Log.info("[CLIENT] Unknown packet received.");
    }
}