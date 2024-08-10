package org.firstinspires.ftc.teamcode.modules.hardware;

import com.qualcomm.robotcore.hardware.Gamepad;

public class GamepadEx {
    Gamepad gamepad;
    Gamepad curGamepad;
    Gamepad prevGamepad;
    public GamepadEx(Gamepad _gamepad){
        gamepad = _gamepad;
        curGamepad = new Gamepad();
        curGamepad.copy(gamepad);
        prevGamepad = new Gamepad();
    }

    /**
     * copy gamepad for rising edge detection
     */
    public void copy(){
        prevGamepad.copy(curGamepad);
        curGamepad.copy(gamepad);
    }

    // buttons
    public boolean cross(){
        return gamepad.cross && !prevGamepad.cross;
    }
    public boolean square(){
        return gamepad.square && !prevGamepad.square;
    }
    public boolean circle(){
        return gamepad.circle && !prevGamepad.circle;
    }
    public boolean triangle(){
        return gamepad.triangle && !prevGamepad.triangle;
    }

    //dpad
    public boolean dpadUp(){
        return gamepad.dpad_up && !prevGamepad.dpad_up;
    }
    public boolean dpadDown(){
        return gamepad.dpad_down && !prevGamepad.dpad_down;
    }
    public boolean dpadLeft(){
        return gamepad.dpad_left && !prevGamepad.dpad_left;
    }
    public boolean dpadRight(){
        return gamepad.dpad_right && !prevGamepad.dpad_right;
    }

    //bumpers
    public boolean leftBumper(){
        return gamepad.left_bumper && !prevGamepad.left_bumper;
    }
    public boolean rightBumper(){
        return gamepad.right_bumper && !prevGamepad.right_bumper;
    }

    //options
    public boolean options(){
        return gamepad.options && !prevGamepad.options;
    }

    //sticks
    public float leftStickX(){
        return gamepad.left_stick_x;
    }
    public float leftStickY(){
        return gamepad.left_stick_y;
    }
    public float rightStickX(){
        return gamepad.right_stick_x;
    }
    public float rightStickY(){
        return gamepad.right_stick_y;
    }

    //triggers
    public float leftTriggerf(){
        return gamepad.left_trigger;
    }
    public boolean leftTriggerb(){
        return (gamepad.left_trigger > 0.5) && !(prevGamepad.left_trigger > 0.5);
    }
    public float rightTriggerf(){
        return gamepad.right_trigger;
    }
    public boolean rightTriggerb(){
        return (gamepad.right_trigger > 0.5) && !(prevGamepad.right_trigger > 0.5);
    }
    public boolean touchpad(){
        return gamepad.touchpad && !prevGamepad.touchpad;
    }
    public boolean share(){return gamepad.share && !prevGamepad.share;}
    public boolean ps(){return gamepad.ps && !prevGamepad.ps;}
}
