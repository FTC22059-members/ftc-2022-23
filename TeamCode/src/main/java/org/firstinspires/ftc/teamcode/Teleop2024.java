package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.modules.Drive;
import org.firstinspires.ftc.teamcode.modules.Imu;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


@TeleOp(name = "Tele-op 2023")
public class Teleop2024 extends LinearOpMode{

    @Override
    public void runOpMode() {
        // init modules
        Global global = new Global(hardwareMap, telemetry, gamepad1, gamepad2);

        Imu robotImu = new Imu(global);
        robotImu.init();
        robotImu.resetAngle();

        Drive driveTrain = new Drive(global, robotImu);
        driveTrain.init();

        telemetry.addLine("Roses are red, violets are blue, if you press start on the robot, then it will move");
        telemetry.update();

        waitForStart();
        boolean accelerationMode = true;

        boolean globalPositioning = false; // is global positioning on?
        double gyroAngle = 0; // the angle that the robot has turned since gp was turned on
        boolean yPrev = false; // whether the y button was pressed in the last frame (useful for detecting button presses)
        boolean xPrev = false; // look literally one line up
        double speedMultiplier = 1; // speed multiplier for acceleration


        while (opModeIsActive()) { // runs until the program is stopped
            // SECTION 1: SETUP
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
            double rightX = gamepad1.right_stick_x / 1.3; // turning, we divide so it's a bit slower
            telemetry.addData("leftX", leftX);
            telemetry.addData("leftY", leftY);
            telemetry.addData("rightX", rightX);


            // SECTION 2: ACCELERATION/PRECISION MODE
            // if precision mode is on (the right trigger is pulled down to some degree)
            if (gamepad1.right_trigger > 0.05 && gamepad1.right_trigger < 0.75) {
                speedMultiplier = 1 - gamepad1.right_trigger;
                telemetry.addData("Precise Mode", "On");
                // also if precision mode is on, but it caps brake at 25% after 75% trigger
            } else if (gamepad1.right_trigger >= 0.75) {
                speedMultiplier = 0.25;
                telemetry.addData("Precise Mode", "On");
            } else { // if precise mode is off, then don't apply brake
                telemetry.addData("Precise Mode", "Off");
                speedMultiplier = 1;
            }
            telemetry.addData("speedMultiplier: ", speedMultiplier);

            // SECTION 3: GLOBAL POSITIONING
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

            // SECTION 4: MOVEMENT/TURNING
            driveTrain.moveRobot(joystickMagnitude, newAngle, rightX, speedMultiplier);

            if (!driveTrain.frontLeft.isBusy() && !driveTrain.frontRight.isBusy() &&
                    !driveTrain.backLeft.isBusy() && !driveTrain.backRight.isBusy()){
                if(gamepad1.dpad_left){
                    driveTrain.snapCcw();
                }else if (gamepad1.dpad_right){
                    driveTrain.snapCw();
                }
            }
            // SECTION 5: DE-SETUPICATOR
            telemetry.update();

            idle();



        }


    }

}
