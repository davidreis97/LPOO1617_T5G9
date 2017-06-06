package com.drfl.twinstickshooter;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

public class NetworkListener extends Listener {

    private static final boolean DEBUG = false;

    @Override
    public void connected(Connection conn) {
        Log.info("[SERVER] Someone connected.");
    }

    @Override
    public void disconnected (Connection conn) {
        Log.info("[SERVER] Someone disconnected.");
    }

    @Override
    public void received (Connection conn, Object obj) {

        if (obj instanceof Packet.ControllerInfoPacket) {
            if(DEBUG) Log.info("[SERVER] ControllerInfoPacket received.");
            TSSServer.processControllerInfo((Packet.ControllerInfoPacket) obj);

        } else {
            if(DEBUG) Log.info("[SERVER] Unknown packet received.");
        }
    }
}