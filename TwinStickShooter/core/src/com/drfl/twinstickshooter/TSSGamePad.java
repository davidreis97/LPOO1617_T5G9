package com.drfl.twinstickshooter;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.minlog.Log;

/**
 * Class responsible for interacting with a XBOX360 controller
 */
public class TSSGamePad implements ControllerListener {

    private static final boolean DEBUG = false;

    private boolean usesController = true;

    private Controller controller;

    /**
     * The singleton instance of this gamepad
     */
    private static TSSGamePad instance;

    public static TSSGamePad getInstance() {
        if (instance == null)
            instance = new TSSGamePad();
        return instance;
    }

    /**
    * Creates a new TSSGamePad, adds it as a listener and detects the connected controllers.
     */
    public TSSGamePad() {

        Controllers.addListener(this);

        if(Controllers.getControllers().size == 0) {
            usesController = false;
        } else {
            controller = Controllers.getControllers().first();
        }
    }

    /**
     * Gets the current data from the left analog stick, if it is outside of the deadzone.
     * @return Vector2 with left stick current position
     */
    public Vector2 getLeftStickVector() {

        Vector2 left = new Vector2(0,0);

        float deadzone = 0.2f;

        //Left Analog Stick
        if(controller.getAxis(XBox360Pad.AXIS_LEFT_X) > deadzone  || controller.getAxis(XBox360Pad.AXIS_LEFT_X) < -deadzone)
            left.x = controller.getAxis(XBox360Pad.AXIS_LEFT_X);
        if(controller.getAxis(XBox360Pad.AXIS_LEFT_Y) > deadzone  || controller.getAxis(XBox360Pad.AXIS_LEFT_Y) < -deadzone)
            left.y = -controller.getAxis(XBox360Pad.AXIS_LEFT_Y); //Inverted

        return left;
    }

    /**
     * Gets the current data from the right analog stick, if it is outside of the deadzone.
     * @return Vector2 with right stick current position
     */
    public Vector2 getRightStickVector() {

        Vector2 right = new Vector2(0,0);

        float deadzone = 0.2f;

        //Right Analog Stick
        if(controller.getAxis(XBox360Pad.AXIS_RIGHT_X) > deadzone || controller.getAxis(XBox360Pad.AXIS_RIGHT_X) < -deadzone)
            right.x = controller.getAxis(XBox360Pad.AXIS_RIGHT_X);
        if(controller.getAxis(XBox360Pad.AXIS_RIGHT_Y) > deadzone  || controller.getAxis(XBox360Pad.AXIS_RIGHT_Y) < -deadzone)
            right.y = -controller.getAxis(XBox360Pad.AXIS_RIGHT_Y);

        return right;
    }

    /**
     * Returns whether a button is pressed.
     *
     * @param button ID of button.
     * @return Whether the button is pressed.
     */
    public boolean getButton(int button) {
        return this.controller.getButton(button);
    }

    /**
     * @return True if a controller is connected
     */
    public boolean controllerExists() {
        return usesController;
    }

    /**
     * Runs when a button is pressed.
     * @param controller The current controller
     * @param buttonCode Integer number that corresponds to the button pressed
     * @return False, so that the event isn't redirected to other handlers.
     */
    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {

        if(DEBUG) Log.info("Button code: " + buttonCode);

        if(buttonCode == XBox360Pad.BUTTON_Y) {
            if(DEBUG) Log.info("Y");
        }else if(buttonCode == XBox360Pad.BUTTON_A) {
            if(DEBUG) Log.info("A");
        }else if(buttonCode == XBox360Pad.BUTTON_X) {
            if(DEBUG) Log.info("X");
        }else if(buttonCode == XBox360Pad.BUTTON_B){
            if(DEBUG) Log.info("B");
        }

        else if(buttonCode == XBox360Pad.BUTTON_LB) {
            if(DEBUG) Log.info("LB");
        }else if(buttonCode == XBox360Pad.BUTTON_RB){
            if(DEBUG) Log.info("RB");
        }

        return false;
    }

    /**
     * Runs when a pov is moved. Currently not in use.
     * @param controller The current controller
     * @param povCode Integer number that corresponds to the pov moved
     * @param value Direction of the moved pov
     * @return False, so that the event isn't redirected to other handlers.
     */
    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {

        if(DEBUG) Log.info("POV: " + value);

        if(value == XBox360Pad.BUTTON_DPAD_LEFT){

        }else if(value == XBox360Pad.BUTTON_DPAD_RIGHT){

        }else if(value == XBox360Pad.BUTTON_DPAD_UP) {

        }else if (value == XBox360Pad.BUTTON_DPAD_DOWN) {

        }
        return false;
    }

    //The following functions are currently not in use.

    /**
     * Changes the variable usesController to true when a controller is connected. (Not working with XBOX360 controller)
     * @param controller The current controller
     */
    @Override
    public void connected(Controller controller) { usesController = true;}

    /**
     * Changes the variable usesController to false when a controller is disconnected. (Not working with XBOX360 controller)
     * @param controller The current controller
     */
    @Override
    public void disconnected(Controller controller) {usesController = false;} //Does NOT work with XBOX360 controller

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) { return false; }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) { return false; }
}