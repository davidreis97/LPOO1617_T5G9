package com.drfl.twinstickshooter.gamepad;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.minlog.Log;

/**
 * Handles gamepad input, namely X360 controller.
 */
public class TSSGamePad implements ControllerListener {

    /**
     * Debug flag.
     */
    private static final boolean DEBUG = false;

    /**
     * Whether a controller is in use.
     */
    private boolean useController = true;

    /**
     * Represents a connected controller. Provides methods to query the state of buttons, axes, POVs, sliders and
     * accelerometers on the controller. Multiple ControllerListener instances can be registered with the Controller
     * to receive events in case the controller's state changes. Listeners will be invoked on the rendering thread.
     */
    private Controller controller;

    /**
     * The singleton instance of the gamepad.
     */
    private static TSSGamePad instance;

    /**
     * Returns a singleton instance of the gamepad.
     *
     * @return The singleton instance
     */
    public static TSSGamePad getInstance() {

        if (instance == null)
            instance = new TSSGamePad();
        return instance;
    }

    /**
     * Constructs a new gamepad instance, adds listener and detects connected controllers.
     */
    private TSSGamePad() {

        Controllers.addListener(this);

        if(Controllers.getControllers().size == 0) {
            useController = false;
        } else {
            controller = Controllers.getControllers().first();
        }
    }

    /**
     * Gets the current data from the left analog stick, if it's outside of the deadzone.
     *
     * @return The vector representing left stick position
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
     * Gets the current data from the right analog stick, if it's outside of the deadzone.
     *
     * @return The vector representing right stick position
     */
    public Vector2 getRightStickVector() {

        Vector2 right = new Vector2(0,0);

        float deadzone = 0.2f;

        //Right Analog Stick
        if(controller.getAxis(XBox360Pad.AXIS_RIGHT_X) > deadzone || controller.getAxis(XBox360Pad.AXIS_RIGHT_X) < -deadzone)
            right.x = controller.getAxis(XBox360Pad.AXIS_RIGHT_X);
        if(controller.getAxis(XBox360Pad.AXIS_RIGHT_Y) > deadzone  || controller.getAxis(XBox360Pad.AXIS_RIGHT_Y) < -deadzone)
            right.y = -controller.getAxis(XBox360Pad.AXIS_RIGHT_Y); //Inverted

        return right;
    }

    /**
     * @param button Button ID to check
     * @return Whether button is pressed
     */
    public boolean getButton(int button) {
        return this.controller.getButton(button);
    }

    /**
     * @return Whether a controller is connected
     */
    public boolean controllerExists() {
        return useController;
    }

    /**
     * A button on the Controller was pressed.
     *
     * @param controller The controller of the button press
     * @param buttonCode Integer number that corresponds to the button pressed
     * @return Whether to hand the event to other listeners
     */
    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {

        if(DEBUG) Log.info("Button code: " + buttonCode);

        if(buttonCode == XBox360Pad.BUTTON_Y) {
            if(DEBUG) Log.info("Y");
        } else if(buttonCode == XBox360Pad.BUTTON_A) {
            if(DEBUG) Log.info("A");
        } else if(buttonCode == XBox360Pad.BUTTON_X) {
            if(DEBUG) Log.info("X");
        } else if(buttonCode == XBox360Pad.BUTTON_B) {
            if(DEBUG) Log.info("B");

        } else if(buttonCode == XBox360Pad.BUTTON_LB) {
            if(DEBUG) Log.info("LB");
        } else if(buttonCode == XBox360Pad.BUTTON_RB) {
            if(DEBUG) Log.info("RB");
        }

        return false;
    }

    /**
     * A POV on the Controller moved.
     *
     * @param controller The controller of the POV movement
     * @param povCode Integer number that corresponds to the POV moved
     * @param value POV direction
     * @return Whether to hand the event to other listeners.
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

    /**
     * A Controller got connected. (X360 controller doesn't notify properly)
     *
     * @param controller The connected controller
     */
    @Override
    public void connected(Controller controller) { useController = true;}

    /**
     * A Controller got disconnected. (X360 controller doesn't notify properly)
     *
     * @param controller The disconnected controller
     */
    @Override
    public void disconnected(Controller controller) { useController = false;}


    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) { return false;}


    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) { return false;}


    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) { return false; }


    @Override
    public boolean buttonUp(Controller controller, int buttonCode) { return false;}


    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) { return false; }
}