
/*
2022-23, Overclocked 22059
*/

package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.library.Arm;
import org.firstinspires.ftc.teamcode.library.Drive;
import org.firstinspires.ftc.teamcode.library.Gripper;
import org.firstinspires.ftc.teamcode.library.Imu;
import com.overclockedftc.typographer.Angles;
import com.overclockedftc.typographer.Arrow;
import com.overclockedftc.typographer.Fill;
import com.overclockedftc.typographer.Frame;
import com.overclockedftc.typographer.LogoAnimation;
import com.overclockedftc.typographer.Orientations;
import com.overclockedftc.typographer.Renderer;
import com.overclockedftc.typographer.Surface;
import com.overclockedftc.typographer.Texel;

//  ▒█████   ██▒   █▓ ▓█████ ██▀███    ▄████▄   ██▓    ▒█████    ▄████▄  ██ ▄█▀ ▓█████▓█████▄
// ▒██▒  ██▒▓██░   █▒ ▓█   ▀▓██ ▒ ██▒ ▒██▀ ▀█  ▓██▒   ▒██▒  ██▒ ▒██▀ ▀█  ██▄█▒  ▓█   ▀▒██▀ ██▌
// ▒██░  ██▒ ▓██  █▒░ ▒███  ▓██ ░▄█ ▒ ▒▓█    ▄ ▒██░   ▒██░  ██▒ ▒▓█    ▄▓███▄░  ▒███  ░██   █▌
// ▒██   ██░  ▒██ █░░ ▒▓█  ▄▒██▀▀█▄  ▒▒▓▓▄ ▄██ ▒██░   ▒██   ██░▒▒▓▓▄ ▄██▓██ █▄  ▒▓█  ▄░▓█▄   ▌
// ░ ████▓▒░   ▒▀█░  ▒░▒████░██▓ ▒██▒░▒ ▓███▀ ▒░██████░ ████▓▒░░▒ ▓███▀ ▒██▒ █▄▒░▒████░▒████▓
// ░ ▒░▒░▒░    ░ ▐░  ░░░ ▒░ ░ ▒▓ ░▒▓░░░ ░▒ ▒  ░░ ▒░▓  ░ ▒░▒░▒░ ░░ ░▒ ▒  ▒ ▒▒ ▓▒░░░ ▒░  ▒▒▓  ▒
//   ░ ▒ ▒░    ░ ░░  ░ ░ ░    ░▒ ░ ▒    ░  ▒  ░░ ░ ▒    ░ ▒ ▒░    ░  ▒  ░ ░▒ ▒░░ ░ ░   ░ ▒  ▒
// ░ ░ ░ ▒        ░      ░    ░░   ░  ░          ░ ░  ░ ░ ░ ▒   ░       ░ ░░ ░     ░   ░ ░  ░
//     ░ ░        ░  ░   ░     ░      ░ ░     ░    ░      ░ ░   ░ ░     ░  ░   ░   ░     ░


@TeleOp(name = "Tele-op 2023 Telemetry")
public class TelemetryTeleop2023 extends LinearOpMode {

    @Override
    public void runOpMode() {
        Renderer fancyTelemetry = new Renderer(telemetry, 34, 15);
        LogoAnimation wordmark = new LogoAnimation();
        Imu robotImu = new Imu(hardwareMap);
        robotImu.init();
        robotImu.resetAngle();

        Drive driveTrain = new Drive(hardwareMap, robotImu);
        driveTrain.init();

        Gripper gripper = new Gripper(hardwareMap);
        gripper.init(gamepad1, gamepad2);

        Arm armMotor = new Arm(hardwareMap);
        armMotor.init(gamepad1, gamepad2);

        while (opModeInInit()) {
            fancyTelemetry.insert(wordmark.getFrame(), 0, 0, false, false, false);
            fancyTelemetry.insert(new Surface(18, 3, " ! ").drawText(1, 1, "Arm Initialized!"), 36, 10, false, false, false);

            fancyTelemetry.loop();
        }

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
                // also if precision mode is on, but it's fully or almost fully pu
            } else if (gamepad1.right_trigger >= 0.75) {
                brakePercent = 0.25;
            } else { // if precise mode is off, and the robot will slowly accelerate
                brakePercent = 1; //Return to default
            }

            // Toggles global position if requested
            if (gamepad1.y && !yPrev) {
                globalPositioning = !globalPositioning;
            }
            yPrev = gamepad1.y;


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

            // get the controls
            double leftX = gamepad1.left_stick_x;
            double leftY = -gamepad1.left_stick_y;
            double rightX = gamepad1.right_stick_x / 1.3;

            if (globalPositioning) { // Adjust direction of movement
                gyroAngle = robotImu.getAngleRadians();
            } else {
                gyroAngle = 0;
            }

            double joystickAngle = Math.atan2(leftX, leftY);
            double newAngle = joystickAngle + gyroAngle;
            double joystickMagnitude = Math.sqrt(Math.pow(leftX, 2) + Math.pow(leftY, 2));

            driveTrain.moveRobot(joystickMagnitude, newAngle, rightX, brakePercent, 0.83);

            if (!driveTrain.frontLeft.isBusy() && !driveTrain.frontRight.isBusy() &&
                    !driveTrain.backLeft.isBusy() && !driveTrain.backRight.isBusy()) {
                if (gamepad1.dpad_left) {
                    driveTrain.snapCcw();
                } else if (gamepad1.dpad_right) {
                    driveTrain.snapCw();
                }
            }

            armMotor.armLoop();

            gripper.gripperLoop();

            //Pauses so acceleration multiplier doesn't ramp up too quick
            //sleep(speedRamp);


//            double leftX = gamepad1.left_stick_x;
//            double leftY = -gamepad1.left_stick_y;
//            double rightX = gamepad1.right_stick_x / 1.3;

            //16 0
            Surface robotContext = new Surface(17, 7, "Robot");

            int roundedAngle = Math.round(Math.round(robotImu.getAngle() * 45.0) / 45);
//            switch (roundedAngle) {
//                case 0:
//                    robotContext.setChar(2, 0, new Texel(new Arrow(Angles.NORTH)), false);
//                    break;
//                case 45:
//                    robotContext.setChar(0, 0, new Texel(new Arrow(Angles.NORTHWEST)), false);
//                    break;
//                case 90:
//                    robotContext.setChar(0, 1, new Texel(new Arrow(Angles.WEST)), false);
//                    break;
//                case 135:
//                    robotContext.setChar(0, 2, new Texel(new Arrow(Angles.SOUTHWEST)), false);
//                    break;
//                case 180:
//                case -180:
//                    robotContext.setChar(2, 2, new Texel(new Arrow(Angles.SOUTH)), false);
//                    break;
//                case -45:
//                    robotContext.setChar(4, 0, new Texel(new Arrow(Angles.NORTHEAST)), false);
//                    break;
//                case -90:
//                    robotContext.setChar(4, 1, new Texel(new Arrow(Angles.EAST)), false);
//                    break;
//                case -135:
//                    robotContext.setChar(4, 2, new Texel(new Arrow(Angles.SOUTHEAST)), false);
//                    break;
//                default:
//                    robotContext.setChar(2, 0, new Texel('?'), false);
//                    break;
//            }
            robotContext.setChar(4, 1, new Texel(new Arrow(Angles.EAST)), false);
            robotContext.setChar(1, 1, new Texel('*'), false);

            robotContext.setChar(12, 0, new Texel(new Fill((int) Math.abs(Math.round(driveTrain.frontLeft.getPower() * 8)), Orientations.VERTICAL)), false)
                    .setChar(12, 2, new Texel(new
                            Fill((int) Math.abs(Math.round(driveTrain.frontRight.getPower() * 8)), Orientations.VERTICAL)), false)
                    .setChar(16, 0, new Texel(new Fill((int) Math.abs(Math.round(driveTrain.backLeft.getPower() * 8)), Orientations.VERTICAL)), false)
                    .setChar(16, 2, new Texel(new Fill((int) Math.abs(Math.round(driveTrain.backRight.getPower() * 8)), Orientations.VERTICAL)), false);

            if (rightX < 0) {
                robotContext.setChar(14, 1, new Texel('\u21ba'), false); // ↺
            } else if (rightX > 0) {
                robotContext.setChar(14, 1, new Texel('\u21bb'), false); // ↻
            }
            if (leftY < 0) {
                robotContext.setChar(14, 2, new Texel(new Arrow(Angles.SOUTH)), false);
            } else if (leftY > 0) {
                robotContext.setChar(14, 0, new Texel(new Arrow(Angles.NORTH)), false);

            }
            if (leftX < 0) {
                robotContext.setChar(16, 1, new Texel(new Arrow(Angles.EAST)), false);
            } else if (leftX > 0) {
                robotContext.setChar(12, 1, new Texel(new Arrow(Angles.WEST)), false);
            }

            robotContext.drawText(0, 3, Math.round(robotImu.getAngle()) + "º")
                    .drawText(5, 3, Math.sqrt(Math.pow(leftX, 2) + Math.pow(leftY, 2)) + "%")
                    .drawCheckbox(1, 5, "Global", globalPositioning)
                    .drawCheckbox(1, 6, "Precise", brakePercent < 1);


            Log.d("TYPOGRAPHER", "|" + armMotor.getArmPosition() + "|");

            //0 0
            Surface armContext = new Surface(9, 14, "Arm");
            armContext.drawGauge(0, 0, 15, armMotor.getArmPosition(), -2500, 43, Orientations.VERTICAL)
                    .drawGauge(1, 0, 15, armMotor.getArmPosition(), -2500, 43, Orientations.VERTICAL)
                    .drawText(3, 0, armMotor.getArmPosition() + "'");

            //16 9
            Surface handOpen = new Surface(16, 7, "Hand");
            handOpen.drawCheckbox(6, 0, "Open", true)
                    .setChar(4, 0, new Texel(new Frame(new int[]{1, 1, 0, 0})), false)
                    .setChar(4, 1, new Texel(new Frame(new int[]{1, 1, 0, 0})), false)
                    .setChar(4, 2, new Texel('\u22a5'), false)

                    .setChar(3, 3, new Texel('\u239f'), false)
                    .setChar(5, 3, new Texel('\u239c'), false);
            Surface handClosed = new Surface(17, 5, "Hand");
            handClosed.drawCheckbox(6, 0, "Open", false)
                    .setChar(4, 0, new Texel(new Frame(new int[]{1, 1, 0, 0})), false)
                    .setChar(4, 1, new Texel(new Frame(new int[]{1, 1, 1, 1})), false)
                    .setChar(4, 2, new Texel(new Frame(new int[]{1, 1, 0, 0})), false)

                    .setChar(3, 1, new Texel('_'), false)
                    .setChar(5, 1, new Texel('_'), false);
//        if (gripper.getIsOpen()) {

            fancyTelemetry.insert(armContext, 0, 0, true, true, true);
            fancyTelemetry.insert(robotContext, 14, 0, true, true, true);
            if (gripper.getIsOpen()) {
                fancyTelemetry.insert(handOpen, 14, 9, true, true, true);
            } else {
                fancyTelemetry.insert(handClosed, 14, 9, true, true, true);
            }

            fancyTelemetry.loop();

            idle();
        }
    }
}