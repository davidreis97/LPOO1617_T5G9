package com.drfl.twinstickshooter;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector2;


public class TSSGamePad implements ControllerListener{

    boolean usesController = true;
    Controller controller;

    public TSSGamePad(){
        Controllers.addListener(this);

        if(Controllers.getControllers().size == 0)
            usesController = false;
        else
            controller = Controllers.getControllers().first();
    }

    public Vector2 getLeftStickVector(){
        Vector2 left = new Vector2(0,0);

        //Left Analog Stick
        if(controller.getAxis(XBox360Pad.AXIS_LEFT_X) > 0.2f  || controller.getAxis(XBox360Pad.AXIS_LEFT_X) < -0.2f)
            left.x = controller.getAxis(XBox360Pad.AXIS_LEFT_X);
        if(controller.getAxis(XBox360Pad.AXIS_LEFT_Y) > 0.2f  || controller.getAxis(XBox360Pad.AXIS_LEFT_Y) < -0.2f)
            left.y = controller.getAxis(XBox360Pad.AXIS_LEFT_X);

        return left;
    }

    public Vector2 getRightStickVector(){
        Vector2 right = new Vector2(0,0);

        //Right Analog Stick
        if(controller.getAxis(XBox360Pad.AXIS_RIGHT_X) > 0.2f  || controller.getAxis(XBox360Pad.AXIS_RIGHT_X) < -0.2f)
            right.x = controller.getAxis(XBox360Pad.AXIS_RIGHT_X);
        if(controller.getAxis(XBox360Pad.AXIS_RIGHT_Y) > 0.2f  || controller.getAxis(XBox360Pad.AXIS_RIGHT_Y) < -0.2f)
            right.y = controller.getAxis(XBox360Pad.AXIS_RIGHT_X);

        return right;
    }

    @Override
    public void connected(Controller controller) {usesController = true;} //Does NOT work with XBOX360 controller

    @Override
    public void disconnected(Controller controller) {usesController = false;} //Does NOT work with XBOX360 controller


    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {

        if(buttonCode == XBox360Pad.BUTTON_Y) {
            //Action
        }else if(buttonCode == XBox360Pad.BUTTON_A) {
            //Action
        }else if(buttonCode == XBox360Pad.BUTTON_X) {
            //Action
        }else if(buttonCode == XBox360Pad.BUTTON_B){
            //Action
        }


        else if(buttonCode == XBox360Pad.BUTTON_LB) {
            //Action
        }else if(buttonCode == XBox360Pad.BUTTON_RB){
            //Action
        }

        //Does not implement all possible buttons, see XBox360Pad.java for more buttons

        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        // This is the dpad
        if(value == XBox360Pad.BUTTON_DPAD_LEFT){
            //Action
        }else if(value == XBox360Pad.BUTTON_DPAD_RIGHT){
            //Action
        }else if(value == XBox360Pad.BUTTON_DPAD_UP) {
            //Action
        }else if (value == XBox360Pad.BUTTON_DPAD_DOWN) {
            //Action
        }
        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        return false;
    }
}
