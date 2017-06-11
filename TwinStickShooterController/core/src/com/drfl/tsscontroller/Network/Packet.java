package com.drfl.tsscontroller.Network;

import com.badlogic.gdx.math.Vector2;

/**
 * Types of packets to send.
 */
public class Packet {

    /**
     * Android controller info packet.
     */
    public static class ControllerInfoPacket {
        public Vector2 movement;
        public Vector2 shooting;
    }
}