
/*
2022-23, Overclocked 22059
*/

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.library.Arm;
import org.firstinspires.ftc.teamcode.library.Drive;

@TeleOp(name = "Tele-op 2023")
public class Teleop2023 extends LinearOpMode {

    @Override
    public void runOpMode() {

        imu robotImu = new imu(hardwareMap, telemetry);
        robotImu.init();

        Drive driveTrain = new Drive(hardwareMap, telemetry);
        driveTrain.init();

        if (!isStopRequested() && !robotImu.isImuCalibrated()) {
            sleep(50);
            idle();
        }
        telemetry.addLine("Imu calibrated!");

        Arm armMotor = new Arm(hardwareMap, telemetry);
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

            if (gamepad1.left_stick_x != 0 || gamepad1.left_stick_y != 0 || gamepad1.right_stick_x != 0) { // if the joystick is moved
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

            telemetry.addData("leftX", leftX);
            telemetry.addData("leftY", leftY);
            telemetry.addData("rightX", rightX);

            //robotImu.imuLoop();
            if (globalPositioning) { // If global positioning is active, adjust direction of movement
                gyroAngle = robotImu.getAngleRadians();
            }



            //double newX = leftX * cos(newAngle) - leftY * sin(newAngle);
            //double newY = leftX * sin(newAngle) + leftY * cos(newAngle);

            double joystickAngle = Math.atan2(leftX, leftY);
            double newAngle = joystickAngle + gyroAngle;
            double joystickMagnitude = Math.sqrt(Math.pow(leftX, 2) + Math.pow(leftY, 2));

            driveTrain.moveRobot(joystickMagnitude, joystickAngle, rightX, speedMultiplier, accelerationMultiplier);

            armMotor.armLoop();
            telemetry.addData("Arm Power", gamepad1.left_stick_y);
            telemetry.update();

            //Pauses so acceleration multiplier doesn't ramp up too quick
            sleep(accelerationModifier);

            idle();
        }
    }
}