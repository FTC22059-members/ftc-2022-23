
/*
2022-23, Overclocked 22059
*/

package org.firstinspires.ftc.teamcode;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name = "GlobalPositioningTest")
public class GlobalPositioningTest extends LinearOpMode {
    
    //Initalize motor variables
    private DcMotorEx backLeft;
    private DcMotorEx backRight;
    private DcMotorEx frontLeft;
    private DcMotorEx frontRight;
    private ElapsedTime timer = new ElapsedTime();
    private float timeTotal = 0;
    
    @Override
    public void runOpMode() {
        //Find motors in hardware map (in the driver station).
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");

        arm armMotorTest = new arm(hardwareMap, telemetry);
        armMotorTest.init(gamepad1, gamepad2);
    
        //Hey, it's PID time
        //Hey, it's still PID time
        
        // Set the pid values to their corresponding wheels
        PIDFCoefficients PIDF = new PIDFCoefficients(10,3,0,0);
        
        backLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, PIDF);
        backRight.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, PIDF);
        frontLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, PIDF);
        frontRight.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, PIDF);
        
        // tell people to press the start button
        telemetry.addData(">", "Roses are red, violets are blue, if you press start on the robot, then it will move");
        telemetry.update();
        
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    
        imu robotImu = new imu(hardwareMap, telemetry);
        robotImu.init();
        
        if (!isStopRequested() && !robotImu.isImuCalibrated()) {
            sleep(50);
            idle();
        }
        telemetry.addData("Imu calibrated!", "\n Let the games begin.");
        telemetry.update();
    
        // Wait till we press the start button
        waitForStart();
        
        // run until the end of the match (driver presses STOP)
        double speedMultiplier = 1; //Default speed
        double accelerationMultiplier = 0; // Currently, it's not accelerating at all
        while (opModeIsActive()) {
            if (gamepad1.right_trigger > 0.05 && gamepad1.right_trigger < 0.75) { // if precision mode is on (the right trigger is pulled down to some degree)
                speedMultiplier = 1-gamepad1.right_trigger;
                telemetry.addData("Precise Mode", "On");
            } else if (gamepad1.right_trigger >= 0.75) { // also if precision mode is on, but it's fully or almost fully pu
                speedMultiplier = 0.25;
                telemetry.addData("Precise Mode", "On");
            } else { // if precise mode is off
                telemetry.addData("Precise Mode", "Off"); // if precision mode is off, and the robot will slowly accelerate
                speedMultiplier = 1; //Return to default
            }
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
            
            // log current data
            telemetry.addData("acceleration multiplier: ", accelerationMultiplier);
            telemetry.addData("speed multiplier: ", speedMultiplier);
            telemetry.addData("real speed multiplier: ", accelerationMultiplier * speedMultiplier);
            telemetry.update();
            
            // get the controls
            double leftX = gamepad1.left_stick_x;
            double leftY = -gamepad1.left_stick_y;
            double rightX = gamepad1.right_stick_x / 1.3;
            
            robotImu.imuLoop();
            double newAngle = robotImu.getAngleRadians();
            double newX = leftX * cos(newAngle) + leftY * sin(newAngle);
            double newY = leftX * sin(newAngle) + leftY * cos(newAngle);

            telemetry.addData("Old X: ",leftX);
            telemetry.addData("Old Y: ",leftY);

            leftX = newX;
            leftY = newY;

            telemetry.addData("New Angle Radians: ",newAngle);
            telemetry.addData("New X: ",newX);
            telemetry.addData("New Y: ",newY);

            // figure out the power for each wheel
            double denominator = Math.max(Math.abs(leftY) + Math.abs(leftX) + Math.abs(rightX), 1);
            double frontLeftPower = (leftY + leftX + rightX) / denominator;
            double backLeftPower = (leftY - leftX + rightX) / denominator;
            double frontRightPower = (leftY - leftX - rightX) / denominator;
            double backRightPower = (leftY + leftX - rightX) / denominator;
            
            // actually tell the wheels to move! (finally)
            backLeft.setPower(backLeftPower * speedMultiplier * accelerationMultiplier);
            backRight.setPower(backRightPower * speedMultiplier * accelerationMultiplier);
            frontLeft.setPower(frontLeftPower * speedMultiplier * accelerationMultiplier);
            frontRight.setPower(frontRightPower * speedMultiplier * accelerationMultiplier);
            // puts this code to sleep, so other code can run
            
            
            if (backLeft.getPower()+backRight.getPower()+frontLeft.getPower()+frontRight.getPower()!=0) {
                timer.reset();
            }
            if (backLeft.getPower()+backRight.getPower()+frontLeft.getPower()+frontRight.getPower()==4) {
                time = timer.milliseconds();
            }
            telemetry.addData("Timer time =", time);
            telemetry.update();

            armMotorTest.armLoop();
            telemetry.addData("Arm Power", gamepad1.left_stick_y);

            idle();
        }
    }
}