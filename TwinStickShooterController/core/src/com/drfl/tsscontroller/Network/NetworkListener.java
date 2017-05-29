package com.drfl.tsscontroller.Network;

/**
 * Created by davidreis on 18/04/2017.
 */

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

public class NetworkListener extends Listener{

    @Override
    public void connected(Connection conn){
        Log.info("[CLIENT] You have connected.");
    }

    @Override
    public void disconnected (Connection conn) {
        Log.info("[CLIENT] You have disconnected.");
    }

    @Override
    public void received (Connection conn, Object obj){
        Log.info("[CLIENT] Unknown packet received.");
    }

}
