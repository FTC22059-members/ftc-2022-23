package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class gripper {
    private Servo gripperServo;

    private Gamepad gamepad1;
    private Gamepad gamepad2;
    private HardwareMap hardwareMap;
    private Telemetry telemetry;

    private static float OPENANGLE = 0.5f;
    private static float CLOSEANGLE = 0f;




    public gripper(HardwareMap hardwareMapCon, Telemetry telemetryCon) {
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

    public void init(Gamepad gamepad1, Gamepad gamepad2) {
        gripperServo = hardwareMap.get(Servo.class, "gripperMotor");
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }

    public void preLoop() {

    }

    public void gripperLoop() {
        if (gamepad1.right_trigger > 0) {
            close();
        } else if (gamepad1.left_trigger > 0) {
            open();
        }
    }
}