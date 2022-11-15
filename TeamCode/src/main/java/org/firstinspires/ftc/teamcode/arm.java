package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class arm {
    public float armPosition = 0;
    private DcMotor armMotor;

    private Gamepad gamepad1;
    private Gamepad gamepad2;
    private HardwareMap hardwareMap;
    private Telemetry telemetry;

    private static float UPPERLIMIT = 10;
    private static float LOWERLIMIT = 0;


    public arm(HardwareMap hardwareMapCon, Telemetry telemetryCon) {
        this.hardwareMap = hardwareMapCon;
        this.telemetry = telemetryCon;
    }

    public void moveUp(float amount) {
        if (armPosition < UPPERLIMIT) {
            armMotor.setPower(amount);
        } else {
            armMotor.setPower(0);
        }
    }

    public void moveDown(float amount) {
        if (armPosition > LOWERLIMIT) {
            armMotor.setPower(amount);
        } else {
            armMotor.setPower(0);
        }
    }

    public void init(Gamepad gamepad1, Gamepad gamepad2) {
        armMotor = hardwareMap.get(DcMotor.class, "armMotor");
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }

    public void preLoop() {

    }

    public void armLoop() {
        if (gamepad1.right_stick_y > 0) {
            moveUp(gamepad1.right_stick_y);
        } else if (gamepad1.right_stick_y < 0) {
            moveDown(gamepad1.right_stick_y);
        } else {
            armMotor.setPower(0);
        }
    }
}
