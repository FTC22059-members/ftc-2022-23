
/*
2022-23, Overclocked 22059
*/

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@TeleOp(name = "Tele-op 2023")
public class Teleop2023 extends LinearOpMode {

    //Initalize motor variables
    private DcMotorEx backLeft;
    private DcMotorEx backRight;
    private DcMotorEx frontLeft;
    private DcMotorEx frontRight;

    @Override
    public void runOpMode() {
        //Find motors in hardware map.
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");

        //Hey, it's PID time

        PIDFCoefficients PIDF = new PIDFCoefficients(10,3,0,0);

        backLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, PIDF);
        backRight.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, PIDF);
        frontLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, PIDF);
        frontRight.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, PIDF);

        // Wait for the start button
        telemetry.addData(">", "Roses are red, violets are blue, if you press start on the robot, then it will move");
        telemetry.update();
        
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        
        waitForStart();
        
        // run until the end of the match (driver presses STOP)
        double speedMultiplier = 1; //Default speed
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

                if (gamepad1.left_stick_x != 0 || gamepad1.left_stick_y != 0) { // if the joystick is moved
                    if (accelerationMultiplier < 1) { // accelerate!
                        accelerationMultiplier = accelerationMultiplier + 0.005;
                    }
                } else { // if the joystick isn't moved, reset the multiplier
                    accelerationMultiplier = 0;
                }
            }
            Overrode value while we test out PID, PID should replace accelerationMultiplier*/
            accelerationMultiplier=1;

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