package com.drfl.twinstickshooter;

/**
 * Created by davidreis on 18/04/2017.
 */

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

public class NetworkListener extends Listener{

    @Override
    public void connected(Connection conn){
        Log.info("[SERVER] Someone connected.");
    }

    @Override
    public void disconnected (Connection conn) {
        Log.info("[SERVER] Someone disconnected.");
    }

    @Override
    public void received (Connection conn, Object obj) {
        if (obj instanceof Packet.ControllerInfoPacket){
            Log.info("[SERVER] ControllerInfoPacket received.");
            TSSServer.processControllerInfo((Packet.ControllerInfoPacket) obj);
        }else{
            Log.info("[SERVER] Unknown packet received.");
        }
    }

}
