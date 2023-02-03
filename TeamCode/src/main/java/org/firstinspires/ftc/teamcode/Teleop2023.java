
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

        // tell people to press the start button
        telemetry.addLine("Roses are red, violets are blue, if you press start on the robot, then it will move");
        telemetry.update();

        // Wait till we press the start button
        waitForStart();

        double brakeMultiplier = 1; //Default speed
        boolean globalPositioning = true; // Is global positioning is active?
        double gyroAngle = 0;
        boolean yPrev = false;
        boolean xPrev = false;

        //pre-loops go here
        //TO-DO: More Pre-loops nedded
        gripper.preLoop();


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // if precision mode is on (the right trigger is pulled down to some degree)
            if (gamepad1.right_trigger > 0.05 && gamepad1.right_trigger < 0.75) {
                brakeMultiplier = 1 - gamepad1.right_trigger;
                telemetry.addData("Precise Mode", "On");
            // also if precision mode is on, but it caps brake at 25% after 75% trigger
            } else if (gamepad1.right_trigger >= 0.75) {
                brakeMultiplier = 0.25;
                telemetry.addData("Precise Mode", "On");
            } else { // if precise mode is off, then don't apply brake
                telemetry.addData("Precise Mode", "Off");
                brakeMultiplier = 1; 
            }
            telemetry.addData("brakeMultiplier: ", brakeMultiplier);

            // Toggles global position if requested
            if (gamepad1.y && !yPrev) {
                globalPositioning = !globalPositioning;
            }
            yPrev = gamepad1.y;
            telemetry.addData("Global Positioning",globalPositioning);

            // Resets gyro angle if requested
            if (gamepad1.x && !xPrev) {
                robotImu.resetAngle();
            }
            xPrev = gamepad1.x;

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
            telemetry.addData("Gyro Angle", robotImu.getAngle());
            telemetry.addData("Joystick Angle", joystickAngle*180/Math.PI);


            driveTrain.moveRobot(joystickMagnitude, newAngle, rightX, brakeMultiplier);

            if (!driveTrain.frontLeft.isBusy() && !driveTrain.frontRight.isBusy() &&
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

            idle();
        }
    }
}