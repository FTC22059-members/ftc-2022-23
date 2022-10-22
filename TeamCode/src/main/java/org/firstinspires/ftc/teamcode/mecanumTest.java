
/*
2020-2021 FIRST Tech Challenge Team 14853
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Tele-op 2022")
public class mecanumTest extends LinearOpMode {
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor carousel;
    private DcMotor arm;
    private Servo spatula;

    @Override
    public void runOpMode() {
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        carousel = hardwareMap.get(DcMotor.class, "carousel");
        arm = hardwareMap.get(DcMotor.class, "arm");
        spatula = hardwareMap.get(Servo.class, "spatula");

        // Wait for the start button
        telemetry.addData(">", "Press Start to energize the robot with electrons that make it MOVE!");
        telemetry.update();

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        //initialize arm
        arm.setTargetPosition(0);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double speedMultiplier = 1; //Multiplier for precision mode.
            if (gamepad1.right_trigger > 0.5) {
                speedMultiplier = 0.25;
                telemetry.addData("Precise Mode", "On");
            } else {
                telemetry.addData("Precise Mode", "Off");
            }

            double leftX = gamepad1.left_stick_x;
            double lefty = -gamepad1.left_stick_y;
            double rightX = gamepad1.right_stick_x / 2;

            double denominator = Math.max(Math.abs(lefty) + Math.abs(leftX) + Math.abs(rightX), 1);
            double frontLeftPower = (lefty + leftX + rightX) / denominator;
            double backLeftPower = (lefty - leftX + rightX) / denominator;
            double frontRightPower = (lefty - leftX - rightX) / denominator;
            double backRightPower = (lefty + leftX - rightX) / denominator;

            backLeft.setPower(backLeftPower * speedMultiplier);
            backRight.setPower(backRightPower * speedMultiplier);
            frontLeft.setPower(frontLeftPower * speedMultiplier);
            frontRight.setPower(frontRightPower * speedMultiplier);

            idle();
        }
    }
}