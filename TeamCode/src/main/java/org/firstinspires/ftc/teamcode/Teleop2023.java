
/*
2022-23, Overclocked 22059
*/

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.library.Arm;
import org.firstinspires.ftc.teamcode.library.Drive;
import org.firstinspires.ftc.teamcode.library.Gripper;
import org.firstinspires.ftc.teamcode.library.Imu;

@TeleOp(name = "Tele-op 2023")
public class Teleop2023 extends LinearOpMode {

    @Override
    public void runOpMode() {

        Imu robotImu = new Imu(hardwareMap, telemetry);
        robotImu.init();
        robotImu.resetAngle();

        Drive driveTrain = new Drive(hardwareMap, telemetry, robotImu);
        driveTrain.init();

        Gripper gripper = new Gripper(hardwareMap, telemetry);
        gripper.init(gamepad1, gamepad2);

        Arm armMotor = new Arm(hardwareMap, telemetry);
        armMotor.init(gamepad1, gamepad2);
        telemetry.addData("Arm Initialized", "!");

        boolean accelToggle;

        // tell people to press the start button
        telemetry.addLine("Roses are red, violets are blue, if you press start on the robot, then it will move");
        telemetry.update();

        // Wait till we press the start button
        waitForStart();

        double brakePercent = 1; //Default speed
        double accelerationMultiplier = 0; // Currently, it's not accelerating at all
        boolean globalPositioning = true; // Is global positioning is active?
        double gyroAngle = 0;
        long speedRamp = 1;
        boolean yPrev = false;
        
        //long speedRamp = (long) (1-gamepad1.left_trigger);


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // if precision mode is on (the right trigger is pulled down to some degree)
            if (gamepad1.right_trigger > 0.05 && gamepad1.right_trigger < 0.75) {
                brakePercent = 1 - gamepad1.right_trigger;
                telemetry.addData("Precise Mode", "On");
            // also if precision mode is on, but it's fully or almost fully pu
            } else if (gamepad1.right_trigger >= 0.75) {
                brakePercent = 0.25;
                telemetry.addData("Precise Mode", "On");
            } else { // if precise mode is off, and the robot will slowly accelerate
                telemetry.addData("Precise Mode", "Off");
                brakePercent = 1; //Return to default
            }

            // Toggles global position if requested
            if (gamepad1.y && !yPrev) {
                globalPositioning = !globalPositioning;
            }
            yPrev = gamepad1.y;

            if (accelToggle = false){
                speedRamp = 0;
            } else {
                speedRamp = 1;
            }

            if (gamepad1.x) {
                accelToggle = !accelToggle;
            }

            telemetry.addData("Speed Ramp", speedRamp);
            telemetry.addData("Global Positioning",globalPositioning);

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

            // log current multiplier data
            telemetry.addData("acceleration multiplier: ", accelerationMultiplier);
            telemetry.addData("speed multiplier: ", brakePercent);
            telemetry.addData("real speed multiplier: ", accelerationMultiplier * brakePercent);

            // get the controls
            double leftX = gamepad1.left_stick_x;
            double leftY = -gamepad1.left_stick_y;
            double rightX = gamepad1.right_stick_x / 1.3;

            telemetry.addData("leftX", leftX);
            telemetry.addData("leftY", leftY);
            telemetry.addData("rightX", rightX);

            if (globalPositioning) { // Adjust direction of movement
                gyroAngle = robotImu.getAngleRadians();
            }else{
                gyroAngle = 0;
            }

            double joystickAngle = Math.atan2(leftX, leftY);
            double newAngle = joystickAngle + gyroAngle;
            double joystickMagnitude = Math.sqrt(Math.pow(leftX, 2) + Math.pow(leftY, 2));

            driveTrain.moveRobot(joystickMagnitude, newAngle, rightX, brakePercent, 0.7);

            if (!driveTrain.frontLeft.isBusy() && !driveTrain.frontRight.isBusy()&&
                !driveTrain.backLeft.isBusy() && !driveTrain.backRight.isBusy()){
                if(gamepad1.dpad_left){
                    driveTrain.snapCcw();
                }else if (gamepad1.dpad_right){
                    driveTrain.snapCw();
                }
            }

            armMotor.armLoop();
            telemetry.addData("Arm Power", gamepad2.left_stick_y);

            gripper.gripperLoop();

            telemetry.update();

            //Pauses so acceleration multiplier doesn't ramp up too quick
            //sleep(speedRamp);

            idle();
        }
    }
}