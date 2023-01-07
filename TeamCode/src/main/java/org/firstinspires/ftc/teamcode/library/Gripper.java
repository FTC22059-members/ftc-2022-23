package org.firstinspires.ftc.teamcode.library;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Gripper {
    private CRServo gripperServo;

    private Gamepad gamepad1;
    private Gamepad gamepad2;
    private HardwareMap hardwareMap;
    private Telemetry telemetry;

    private static float OPENANGLE = 0.5f;
    private static float CLOSEANGLE = 0f;




    public Gripper(HardwareMap hardwareMapCon, Telemetry telemetryCon) {
        this.hardwareMap = hardwareMapCon;
        this.telemetry = telemetryCon;
    }


    public void open() {
//        if (gripperServo.getPosition() != OPENANGLE) {
//            gripperServo.setPosition(OPENANGLE);
//        }
    }

    public void close() {
//        if (gripperServo.getPosition() != CLOSEANGLE) {
//            gripperServo.setPosition(CLOSEANGLE);
//        }
    }

    public void init(Gamepad gamepad1, Gamepad gamepad2) {
        gripperServo = hardwareMap.get(CRServo.class, "gripperMotor");
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }

    public void preLoop() {

    }

    public void gripperLoop() {
        if (gamepad2.right_trigger > 0) {
            close();
        } else if (gamepad2.left_trigger > 0) {
            open();
        }
    }
}