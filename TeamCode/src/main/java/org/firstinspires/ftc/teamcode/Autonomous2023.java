
/*
2020-2021 FIRST Tech Challenge Team 14853
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.library.AprilTags;
import org.firstinspires.ftc.teamcode.library.Drive;
import org.firstinspires.ftc.teamcode.library.Imu;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Autonomous 2023")
public class Autonomous2023 extends LinearOpMode {

    Drive driveTrain;

    @Override
    public void runOpMode() {

        Imu robotImu = new Imu(hardwareMap, telemetry);
        robotImu.init();

        driveTrain = new Drive(hardwareMap, telemetry, robotImu);
        driveTrain.init();

        AprilTags aprilTags = new AprilTags(hardwareMap, telemetry);
        aprilTags.init();

        // Wait for the start button

        telemetry.addData(">", "Press Start to energize the robot with electrons that make it MOVE!");
        telemetry.update();
        while (opModeInInit()) {
            aprilTags.detectTag();
        }

        aprilTags.preLoop();

        String zone = "Not found";
        ElapsedTime timer = new ElapsedTime(0);
        timer.reset();
        while (zone.equals("Not found")&&opModeIsActive()){
            // if we're taking too long, get out of loop
            if (timer.seconds()>5){
                zone = "B";
            }else{
                zone = aprilTags.detectTag();
            }
        }

        telemetry.addData("Time to find: ", timer.milliseconds());

        if (zone.equals("A")){
            driveForward(26 , 0.8);
            driveTrain.turn(80);
            driveForward(22.5,0.8);
        }else if (zone.equals("B")){
            driveForward(28, 0.8);
        }else {
            driveForward(26, 0.8);
            driveTrain.turn(-80);
            driveForward(22.5,0.8);
        }
        idle();
    }


    public void driveForward(double distance, double speed){
        moveAutonomous(distance, distance, distance, distance, speed);
    }

    public void driveBackward(double distance, double speed){
        moveAutonomous(-distance, -distance, -distance, -distance, speed);
    }

    public void strafeLeft(double distance, double speed){
        moveAutonomous(-distance, distance, distance, -distance, speed);
    }

    public void strafeRight(double distance, double speed){
        moveAutonomous(distance, -distance, -distance, distance, speed);
    }

    public void moveAutonomous(double frontLeftDistance,
                               double frontRightDistance,
                               double backLeftDistance,
                               double backRightDistance,
                               double power) {
        // Ensure that the opmode is still active
        if (opModeIsActive()) {
            //telemetry.addData("CPI", driveTrain.getCountsPerInch());
            //telemetry.addData("FLD", frontLeftDistance);
            //telemetry.update();
            int frontLeftCounts = (int)(frontLeftDistance * driveTrain.getCountsPerInch());
            int frontRightCounts = (int)(frontRightDistance * driveTrain.getCountsPerInch());
            int backLeftCounts = (int)(backLeftDistance * driveTrain.getCountsPerInch());
            int backRightCounts = (int)(backRightDistance * driveTrain.getCountsPerInch());

            int frontLeftTarget = driveTrain.frontLeft.getCurrentPosition()+frontLeftCounts;
            int frontRightTarget = driveTrain.frontRight.getCurrentPosition()+frontRightCounts;
            int backLeftTarget = driveTrain.backLeft.getCurrentPosition()+backLeftCounts;
            int backRightTarget = driveTrain.backRight.getCurrentPosition()+backRightCounts;

            // Determine new target position, and pass to motor controller
            // Set Target FIRST, then turn on RUN_TO_POSITION
            driveTrain.frontLeft.setTargetPosition(frontLeftTarget);
            driveTrain.frontRight.setTargetPosition(frontRightTarget);
            driveTrain.backLeft.setTargetPosition(backLeftTarget);
            driveTrain.backRight.setTargetPosition(backRightTarget);

            driveTrain.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            driveTrain.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            driveTrain.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            driveTrain.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // Set the required driving speed  (must be positive for RUN_TO_POSITION)
            // Start driving straight, and then enter the control loop
            driveTrain.frontLeft.setPower(power);
            driveTrain.frontRight.setPower(power);
            driveTrain.backLeft.setPower(power);
            driveTrain.backRight.setPower(power);

            // keep looping while we are still active, and ALL motors are running.
            while (opModeIsActive() &&
            (driveTrain.frontLeft.isBusy() && driveTrain.frontRight.isBusy())&&
            driveTrain.backLeft.isBusy() && driveTrain.backRight.isBusy()) {
                telemetry.addData("BL Motor Pos", driveTrain.backLeft.getCurrentPosition());
                telemetry.addData("BL Target Pos", driveTrain.backLeft.getTargetPosition());
                telemetry.addData("BR Motor Pos", driveTrain.backRight.getCurrentPosition());
                telemetry.addData("BR Target Pos", driveTrain.backRight.getTargetPosition());
                telemetry.addData("FL Motor Pos", driveTrain.frontLeft.getCurrentPosition());
                telemetry.addData("FL Target Pos", driveTrain.frontLeft.getTargetPosition());
                telemetry.addData("FR Motor Pos", driveTrain.frontRight.getCurrentPosition());
                telemetry.addData("FR Target Pos", driveTrain.frontRight.getTargetPosition());
                telemetry.update();
            }

            // Stop all motion & Turn off RUN_TO_POSITION
            driveTrain.backRight.setPower(0);
            driveTrain.backLeft.setPower(0);
            driveTrain.frontRight.setPower(0);
            driveTrain.frontLeft.setPower(0);

            driveTrain.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            driveTrain.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            driveTrain.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            driveTrain.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
}
