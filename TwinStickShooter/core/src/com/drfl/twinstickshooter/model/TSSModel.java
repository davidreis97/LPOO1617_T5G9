package com.drfl.twinstickshooter.model;

import com.drfl.twinstickshooter.model.entities.MainChar;

import static com.drfl.twinstickshooter.view.TSSView.TILESIZE;

/**
 * TSS world model
 */

public class TSSModel {
    /**
     * The singleton instance of the game model
     */
    private static TSSModel instance;

    /**
     * Character controlled by the player
     */
    private MainChar mc;

    /**
     * Constructs game with main character //TODO expand game model
     */
    private TSSModel() {

        mc = new MainChar(2.0f - 0.5f, 2.0f + 0.5f, 0); //TODO add initial coords
    }

    /**
     * Returns a singleton instance of the game model
     *
     * @return the singleton instance
     */
    public static TSSModel getInstance() {

        if (instance == null)
            instance = new TSSModel();
        return instance;
    }

    /**
     * @return the main character model.
     */
    public MainChar getMainChar() {
        return mc;
    }
}