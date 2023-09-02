package org.firstinspires.ftc.teamcode;

//  ▒█████   ██▒   █▓ ▓█████ ██▀███    ▄████▄   ██▓    ▒█████    ▄████▄  ██ ▄█▀ ▓█████▓█████▄
// ▒██▒  ██▒▓██░   █▒ ▓█   ▀▓██ ▒ ██▒ ▒██▀ ▀█  ▓██▒   ▒██▒  ██▒ ▒██▀ ▀█  ██▄█▒  ▓█   ▀▒██▀ ██▌
// ▒██░  ██▒ ▓██  █▒░ ▒███  ▓██ ░▄█ ▒ ▒▓█    ▄ ▒██░   ▒██░  ██▒ ▒▓█    ▄▓███▄░  ▒███  ░██   █▌
// ▒██   ██░  ▒██ █░░ ▒▓█  ▄▒██▀▀█▄  ▒▒▓▓▄ ▄██ ▒██░   ▒██   ██░▒▒▓▓▄ ▄██▓██ █▄  ▒▓█  ▄░▓█▄   ▌
// ░ ████▓▒░   ▒▀█░  ▒░▒████░██▓ ▒██▒░▒ ▓███▀ ▒░██████░ ████▓▒░░▒ ▓███▀ ▒██▒ █▄▒░▒████░▒████▓
// ░ ▒░▒░▒░    ░ ▐░  ░░░ ▒░ ░ ▒▓ ░▒▓░░░ ░▒ ▒  ░░ ▒░▓  ░ ▒░▒░▒░ ░░ ░▒ ▒  ▒ ▒▒ ▓▒░░░ ▒░  ▒▒▓  ▒
//   ░ ▒ ▒░    ░ ░░  ░ ░ ░    ░▒ ░ ▒    ░  ▒  ░░ ░ ▒    ░ ▒ ▒░    ░  ▒  ░ ░▒ ▒░░ ░ ░   ░ ▒  ▒
// ░ ░ ░ ▒        ░      ░    ░░   ░  ░          ░ ░  ░ ░ ░ ▒   ░       ░ ░░ ░     ░   ░ ░  ░
//     ░ ░        ░  ░   ░     ░      ░ ░     ░    ░      ░ ░   ░ ░     ░  ░   ░   ░     ░

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "Tele-op 2023")
public class FancyMecanum extends LinearOpMode {

    private DcMotorEx frontLeft;
    private DcMotorEx frontRight;
    private DcMotorEx backLeft;
    private DcMotorEx backRight;

    static final double WHEEL_RADIUS = 25;
    static final double CHASSIS_LENGTH = 25; // distance between the front wheels and the back wheels
    static final double CHASSIS_WIDTH = 25; // distance between the 2 front wheels or 2 back wheels

    @Override
    public void runOpMode() {
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");


        // tell people to press the start button
        telemetry.addLine("Roses are red, violets are blue, if you press start on the robot, then it will move");
        telemetry.update();

        // Wait till we press the start button
        waitForStart();


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double leftX = gamepad1.left_stick_x;
            double leftY = -gamepad1.left_stick_y;
            double rightX = gamepad1.right_stick_x / 1.3;
            telemetry.addData("leftX", leftX);
            telemetry.addData("leftY", leftY);
            telemetry.addData("rightX", rightX);

            double frontLeftMotorPower = (1/WHEEL_RADIUS) * (leftX - leftY - (CHASSIS_WIDTH + CHASSIS_LENGTH) * rightX);
            double frontRightMotorPower = (1/WHEEL_RADIUS) * (leftX + leftY + (CHASSIS_WIDTH + CHASSIS_LENGTH) * rightX);
            double backLeftMotorPower = (1/WHEEL_RADIUS) * (leftX + leftY - (CHASSIS_WIDTH + CHASSIS_LENGTH) * rightX);
            double backRightMotorPower = (1/WHEEL_RADIUS) * (leftX - leftY + (CHASSIS_WIDTH + CHASSIS_LENGTH) * rightX);


            telemetry.addData("frontLeftMotorPower", frontLeftMotorPower);
            telemetry.addData("frontRightMotorPower", frontRightMotorPower);
            telemetry.addData("backLeftMotorPower", backLeftMotorPower);
            telemetry.addData("backRightMotorPower", backRightMotorPower);

            frontLeft.setPower(frontLeftMotorPower);
            frontRight.setPower(frontRightMotorPower);
            backLeft.setPower(backLeftMotorPower);
            backRight.setPower(backRightMotorPower);

            telemetry.update();

            //Pauses so acceleration multiplier doesn't ramp up too quick
            //sleep(speedRamp);

            idle();
        }
    }
}
