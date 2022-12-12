package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class arm {
    private DcMotor armMotor;

    private Gamepad gamepad1;
    private Gamepad gamepad2;
    private HardwareMap hardwareMap;
    private Telemetry telemetry;

    private static float UPPERLIMIT = -2250;
    private static float LOWERLIMIT = 0;


    public arm(HardwareMap hardwareMapCon, Telemetry telemetryCon) {
        this.hardwareMap = hardwareMapCon;
        this.telemetry = telemetryCon;
    }

    public void moveDown(float amount) {
        if (armMotor.getCurrentPosition() < LOWERLIMIT) {
            armMotor.setPower(amount);
        } else {
            armMotor.setPower(0);
        }
    }

    public void moveUp(float amount) {
        if (armMotor.getCurrentPosition() > UPPERLIMIT) {
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
        telemetry.addData("Right stick Y:",gamepad1.right_stick_y);
        if (gamepad1.right_stick_y < 0) {
            telemetry.addData("moving:","up");
            moveUp(gamepad1.right_stick_y);
        } else if (gamepad1.right_stick_y > 0) {
            telemetry.addData("moving:","down");
            moveDown(gamepad1.right_stick_y);
        } else {
            armMotor.setPower(0);
        }
        telemetry.addData("Arm current position:", armMotor.getCurrentPosition());
    }
}
