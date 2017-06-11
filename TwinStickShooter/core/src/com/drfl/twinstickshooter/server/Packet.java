package com.drfl.twinstickshooter.server;

import com.badlogic.gdx.math.Vector2;

/**
 * Types of packet expected from remote connections.
 */
public class Packet {

    /**
     * Android controller info packet.
     */
    static class ControllerInfoPacket {
        Vector2 movement;
        Vector2 shooting;
    }
}