
/*
2022-23, Overclocked 22059
*/

package org.firstinspires.ftc.teamcode;

import static java.lang.Math.sin;
import static java.lang.Math.cos;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Tele-op 2023")
public class Teleop2023 extends LinearOpMode {

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

        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        //Hey, it's PID time
        // Set the pid values to their corresponding wheels
        PIDFCoefficients PIDF = new PIDFCoefficients(10,3,0,0);

        backLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, PIDF);
        backRight.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, PIDF);
        frontLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, PIDF);
        frontRight.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, PIDF);

        imu robotImu = new imu(hardwareMap, telemetry);
        robotImu.init();

        if (!isStopRequested() && !robotImu.isImuCalibrated()) {
            sleep(50);
            idle();
        }
        telemetry.addLine("Imu calibrated!");

        arm armMotor = new arm(hardwareMap, telemetry);
        armMotor.init(gamepad1, gamepad2);
        telemetry.addData("Arm Initialized", "!");

        // tell people to press the start button
        telemetry.addLine("Roses are red, violets are blue, if you press start on the robot, then it will move");
        telemetry.update();

        // Wait till we press the start button
        waitForStart();

        double speedMultiplier = 1; //Default speed
        double accelerationMultiplier = 0; // Currently, it's not accelerating at all
        boolean globalPositioning = true; // If global positioning is active
        double gyroAngle = 0;
        long accelerationModifier = (long) (1-gamepad1.left_trigger);

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            if (gamepad1.right_trigger > 0.05 && gamepad1.right_trigger < 0.75) { // if precision mode is on (the right trigger is pulled down to some degree)
                speedMultiplier = 1 - gamepad1.right_trigger;
                telemetry.addData("Precise Mode", "On");
            } else if (gamepad1.right_trigger >= 0.75) { // also if precision mode is on, but it's fully or almost fully pu
                speedMultiplier = 0.25;
                telemetry.addData("Precise Mode", "On");
            } else { // if precise mode is off
                telemetry.addData("Precise Mode", "Off"); // if precision mode is off, and the robot will slowly accelerate
                speedMultiplier = 1; //Return to default
            }

            if (gamepad1.y) { // Toggles global positioning
                globalPositioning = !globalPositioning;
            }

            /*
            While precise mode is on, if the left stick is moved, incrementally
            increase the speed for about 2/3 of a second, until the speed is at
            its maximum. When the joystick is not pushed, reset speed to 0.
            */

            if (gamepad1.left_stick_x != 0 || gamepad1.left_stick_y != 0) { // if the joystick is moved
                if (accelerationMultiplier < 1) { // accelerate!
                    accelerationMultiplier = accelerationMultiplier + 0.01;
                }
            } else { // if the joystick isn't moved, reset the multiplier
                accelerationMultiplier = 0;
            }

            // log current data
            telemetry.addData("acceleration multiplier: ", accelerationMultiplier);
            telemetry.addData("speed multiplier: ", speedMultiplier);
            telemetry.addData("real speed multiplier: ", accelerationMultiplier * speedMultiplier);
            telemetry.update();

            // get the controls
            double leftX = gamepad1.left_stick_x;
            double leftY = -gamepad1.left_stick_y;
            double rightX = gamepad1.right_stick_x / 1.3;

            telemetry.addData("leftx", leftX);
            telemetry.addData("lefty", leftY);


            //robotImu.imuLoop();
            if (globalPositioning) { // If global positioning is active, adjust direction of movement
                gyroAngle = robotImu.getAngleRadians();
            }

            //double newX = leftX * cos(newAngle) - leftY * sin(newAngle);
            //double newY = leftX * sin(newAngle) + leftY * cos(newAngle);

            double joystickAngle = Math.atan2(leftX, leftY);
            double newAngle = joystickAngle + gyroAngle;
            double joystickMagnitude = Math.sqrt(Math.pow(leftX, 2) + Math.pow(leftY, 2));

            double newX = joystickMagnitude * sin(newAngle);
            double newY = joystickMagnitude * cos(newAngle);

            leftX = newX;
            leftY = newY;
            telemetry.addData("newx", leftX);
            telemetry.addData("newy", leftY);
            telemetry.addData("Angle (Radians)", newAngle);

            // figure out the power for each wheel
            double denominator = Math.max(Math.abs(leftY) + Math.abs(leftX) + Math.abs(rightX), 1);
            double frontLeftPower = (leftY + leftX + rightX) / denominator;
            double backLeftPower = (leftY - leftX + rightX) / denominator;
            double frontRightPower = (leftY - leftX - rightX) / denominator;
            double backRightPower = (leftY + leftX - rightX) / denominator;

            //accelerationMultiplier=Math.pow(Math.abs(frontLeftPower), 2.5-gamepad1.left_trigger*1.5);

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

            armMotor.armLoop();
            telemetry.addData("Arm Power", gamepad1.left_stick_y);
            telemetry.update();

            //Pauses so acceleration multiplier doesn't ramp up too quick
            sleep(accelerationModifier);

            idle();
        }
    }
}