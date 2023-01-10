package org.firstinspires.ftc.teamcode.library;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

// flat gripper (open) = -66
// closed = -96
// their 90 is our 0
public class Gripper {
    private Servo gripperServo;

    private Gamepad gamepad1;
    private Gamepad gamepad2;
    private HardwareMap hardwareMap;
    private Telemetry telemetry;

    private static float OPENANGLE = 1;
    private static float CLOSEANGLE = 0.85f;
    
    private static boolean ISOPEN = true;




    public Gripper(HardwareMap hardwareMapCon, Telemetry telemetryCon) {
        this.hardwareMap = hardwareMapCon;
        this.telemetry = telemetryCon;
    }


    public void open() {
        if (gripperServo.getPosition() != OPENANGLE) {
            gripperServo.setPosition(OPENANGLE);
        }
    }

    public void close() {
        if (gripperServo.getPosition() != CLOSEANGLE) {
            gripperServo.setPosition(CLOSEANGLE);
        }
    }
    
    public boolean getIsOpen() {
        return ISOPEN;
    }


    public void init(Gamepad gamepad1, Gamepad gamepad2) {
        gripperServo = hardwareMap.get(Servo.class, "gripperMotor");
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }

    public void preLoop() {

    }

    public void gripperLoop() {
        if (gamepad2.right_trigger > 0 && ISOPEN) {
            close();
            ISOPEN = false;
        } else if (gamepad2.left_trigger > 0 && !ISOPEN) {
            open();
            ISOPEN = true;
        }
    }
}