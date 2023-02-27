package org.firstinspires.ftc.teamcode.library;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Arm {
    private DcMotor armMotor;

    private Gamepad gamepad1;
    private Gamepad gamepad2;
    private HardwareMap hardwareMap;

    private static float UPPERLIMIT = -2500;
    private static float LOWERLIMIT = 0;


    public Arm(HardwareMap hardwareMapCon) {
        this.hardwareMap = hardwareMapCon;
    }

    /**
     * Init method for the drive.java class. Imports and sets up various things, currently the gamepads
     * @param gamepad1 The driver gamepad
     * @param gamepad2 The arm gamepad
     */

    public void init(Gamepad gamepad1, Gamepad gamepad2) {
        armMotor = hardwareMap.get(DcMotor.class, "armMotor");
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }

    /**
     * This method moves the arm down by a set amount
     * @param amount The amount that the arm moves down
     */
    public void moveDown(float amount) {
        if (armMotor.getCurrentPosition() < LOWERLIMIT) {
            armMotor.setPower(amount);
        } else {
            armMotor.setPower(0);
        }
    }

    /**
     * This method moves the arm up by a set amount
     * @param amount The amount that the arm moves up
     */
    public void moveUp(float amount) {
        if (armMotor.getCurrentPosition() > UPPERLIMIT) {
            armMotor.setPower(amount);
        } else {
            armMotor.setPower(0);
        }
    }

    /**
     * This method executes before the main loop in the program.
     * This currently doesn't have any real use, it's more here for principle's sake
     **/

    public void preLoop() {

    }

    /**
     * This method executes during the main loop in the program.
     * Currently adds telemetry to the console.
     **/

    public void armLoop() {
        if (gamepad2.left_stick_y < 0) {
            moveUp(gamepad2.left_stick_y);
        } else if (gamepad2.left_stick_y > 0) {
            moveDown(gamepad2.left_stick_y);
        } else {
            armMotor.setPower(0);
        }
    }

    public int getArmPosition() {
        return armMotor.getCurrentPosition();
    }
}
