
/*
2022-23, Overclocked 22059
*/

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.library.Arm;
import org.firstinspires.ftc.teamcode.library.Drive;
import org.firstinspires.ftc.teamcode.library.Gripper;
import org.firstinspires.ftc.teamcode.library.Imu;
import org.firstinspires.ftc.teamcode.library.fancyTelemetry.*;

@TeleOp(name = "Tele-op 2023")
public class TelemetryTeleop2023 extends LinearOpMode {

    @Override
    public void runOpMode() {
        RenderTelemetry fancyTelemetry = new RenderTelemetry(telemetry);
        GraphicsContext display = fancyTelemetry.display;

        Imu robotImu = new Imu(hardwareMap);
        robotImu.init();
        robotImu.resetAngle();

        Drive driveTrain = new Drive(hardwareMap, robotImu);
        driveTrain.init();

        Gripper gripper = new Gripper(hardwareMap);
        gripper.init(gamepad1, gamepad2);

        Arm armMotor = new Arm(hardwareMap);
        armMotor.init(gamepad1, gamepad2);
        telemetry.addData("Arm Initialized", "!");

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

            idle();
        }

        double leftX = gamepad1.left_stick_x;
        double leftY = -gamepad1.left_stick_y;
        double rightX = gamepad1.right_stick_x / 1.3;

        GraphicsContext robotContext = display.addContext(16, 0, 16, 9, "Robot");

        int roundedAngle = Math.round(Math.round(robotImu.getAngle() * 45.0) / 45);
        switch (roundedAngle) {
            case 0:
                robotContext.setChar(2, 0, new Texel(new Arrow(Angles.NORTH)), false, false);
            case 45:
                robotContext.setChar(0, 0, new Texel(new Arrow(Angles.NORTHWEST)), false, false);
            case 90:
                robotContext.setChar(0, 1, new Texel(new Arrow(Angles.WEST)), false, false);
            case 135:
                robotContext.setChar(0, 2, new Texel(new Arrow(Angles.SOUTHWEST)), false, false);
            case 180:
                robotContext.setChar(2, 2, new Texel(new Arrow(Angles.SOUTH)), false, false);
            case -180:
                robotContext.setChar(2, 2, new Texel(new Arrow(Angles.SOUTH)), false, false);
            case -45:
                robotContext.setChar(4, 0, new Texel(new Arrow(Angles.NORTHEAST)), false, false);
            case -90:
                robotContext.setChar(4, 1, new Texel(new Arrow(Angles.EAST)), false, false);
            case -135:
                robotContext.setChar(4, 2, new Texel(new Arrow(Angles.SOUTHEAST)), false, false);
            default:
                robotContext.setChar(2, 0, new Texel("?"), false, false);
        }
        robotContext.setChar(1, 1, new Texel("*"), false, false);

        robotContext.setChar(8, 0, new Texel(new Fill((int) Math.round(driveTrain.frontLeft.getPower() * 8), Orientations.VERTICAL)), false, false)
                .setChar(8, 2, new Texel(new Fill((int) Math.round(driveTrain.frontRight.getPower() * 8), Orientations.VERTICAL)), false, false)
                .setChar(12, 0, new Texel(new Fill((int) Math.round(driveTrain.backLeft.getPower() * 8), Orientations.VERTICAL)), false, false)
                .setChar(12, 2, new Texel(new Fill((int) Math.round(driveTrain.backRight.getPower() * 8), Orientations.VERTICAL)), false, false);

        if (rightX < 0) {
            robotContext.setChar(10, 1, new Texel("\u21ba"), false, false); // ↺
        } else if (rightX > 0) {
            robotContext.setChar(10, 1, new Texel("\u21bb"), false, false); // ↻
        }
        if (leftY < 0) {
            robotContext.setChar(10, 2, new Texel(new Arrow(Angles.SOUTH)), false, false);
        } else if (leftY > 0) {
            robotContext.setChar(10, 0, new Texel(new Arrow(Angles.NORTH)), false, false);

        }
        if (leftX < 0) {
            robotContext.setChar(12, 1, new Texel(new Arrow(Angles.EAST)), false, false);
        } else if (leftX > 0) {
            robotContext.setChar(8, 1, new Texel(new Arrow(Angles.WEST)), false, false);
        }

        robotContext.drawText(0, 3, Math.round(robotImu.getAngle()) + "º", false)
                .drawText(5, 3, Math.sqrt(Math.pow(leftX, 2) + Math.pow(leftY, 2)) + "%", false)
                .drawCheckbox(1, 5, "Global", globalPositioning)
                .drawCheckbox(1, 6, "Precise", brakePercent < 1);

        display.addContext(0, 0, 16, 16, "Arm")
                .drawGauge(0, 0, 15, armMotor.getArmPosition(), -2500, 0, Orientations.VERTICAL)
                .drawGauge(1, 0, 15, armMotor.getArmPosition(), -2500, 0, Orientations.VERTICAL)
                .drawText(3, 0, armMotor.getArmPosition() + "'", false);

        if (gripper.getIsOpen()) {
            display.addContext(16, 9, 16, 7, "Hand")
                    .drawCheckbox(6, 0, "Open", true)
                    .setChar(4, 0, new Texel(new Frame(new int[]{1, 1, 0, 0})), false, false)
                    .setChar(4, 1, new Texel(new Frame(new int[]{1, 1, 0, 0})), false, false)
                    .setChar(4, 2, new Texel("\u22a5"), false, false)

                    .setChar(3, 3, new Texel("\u239f"), false, false)
                    .setChar(5, 3, new Texel("\u239c"), false, false);
        } else {
            display.addContext(16, 9, 16, 7, "Hand")
                    .drawCheckbox(6, 0, "Open", false)
                    .setChar(4, 0, new Texel(new Frame(new int[]{1, 1, 0, 0})), false, false)
                    .setChar(4, 1, new Texel(new Frame(new int[]{1, 1, 1, 1})), false, false)
                    .setChar(4, 2, new Texel(new Frame(new int[]{1, 1, 0, 0})), false, false)

                    .setChar(3, 1, new Texel("_"), false, false)
                    .setChar(5, 1, new Texel("_"), false, false);
        }

        fancyTelemetry.loop();
    }
}