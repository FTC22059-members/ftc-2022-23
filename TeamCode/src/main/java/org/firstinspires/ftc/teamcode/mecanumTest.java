
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

        // Wait for the start button
        telemetry.addData(">", "Press Start to energize the robot with electrons that make it MOVE!");
        telemetry.update();

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);


        waitForStart();

        // run until the end of the match (driver presses STOP)
        double speedMultiplier = 1; //Default speed
        double accelerationTime = 0;
        double accelerationMultiplier = 0;
        while (opModeIsActive()) {
            if (gamepad1.right_trigger > 0.05 && gamepad1.right_trigger < 0.75) {
                speedMultiplier = 1-gamepad1.right_trigger;
                telemetry.addData("Precise Mode", "On");
            } else if (gamepad1.right_trigger >= 0.75) {
                speedMultiplier = 0.25;
                telemetry.addData("Precise Mode", "On");
            } else { // if precise mode is off
                telemetry.addData("Precise Mode", "Off");
                speedMultiplier = 1; //Return to default
                /*
                While precise mode is off, if the left stick is moved, incrementally
                increase the speed for about 2/3 of a second, until the speed is at
                its maximum. When the joystick is not pushed, reset speed to 0.
                */
                if (gamepad1.left_stick_x != 0 || gamepad1.left_stick_y != 0) { // if the joystick is moved
                    if (accelerationMultiplier < 1) { // accelerate!
                        accelerationMultiplier = accelerationMultiplier + 0.05;
                    }
                } else { // if the joystick isn't moved, reset the multiplier
                    accelerationMultiplier = 0;
                }
            }

            telemetry.addData("acceleration multiplier: ", accelerationMultiplier);
            telemetry.addData("speed multiplier: ", speedMultiplier);
            telemetry.addData("real speed multiplier: ", accelerationMultiplier * speedMultiplier);
            telemetry.update();

            double leftX = gamepad1.left_stick_x;
            double lefty = -gamepad1.left_stick_y;
            double rightX = gamepad1.right_stick_x / 1.3;

            double denominator = Math.max(Math.abs(lefty) + Math.abs(leftX) + Math.abs(rightX), 1);
            double frontLeftPower = (lefty + leftX + rightX) / denominator;
            double backLeftPower = (lefty - leftX + rightX) / denominator;
            double frontRightPower = (lefty - leftX - rightX) / denominator;
            double backRightPower = (lefty + leftX - rightX) / denominator;

            backLeft.setPower(backLeftPower * speedMultiplier * accelerationMultiplier);
            backRight.setPower(backRightPower * speedMultiplier * accelerationMultiplier);
            frontLeft.setPower(frontLeftPower * speedMultiplier * accelerationMultiplier);
            frontRight.setPower(frontRightPower * speedMultiplier * accelerationMultiplier);

            idle();
        }
    }
}
