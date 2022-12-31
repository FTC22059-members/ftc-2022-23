/* Copyright (c) 2022 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;



@Autonomous(name="RIBS: Robot Informatory Basis Script", group="Robot")
public class AutonomousRibs extends LinearOpMode {

    drive driveTrain;

    @Override
    public void runOpMode() {

        // define initialization values for IMU, and then initialize it.
        //BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        //parameters.angleUnit            = BNO055IMU.AngleUnit.DEGREES;
        //imu = hardwareMap.get(BNO055IMU.class, "imu");
        //imu.initialize(parameters);

        imu robotImu = new imu(hardwareMap, telemetry);
        robotImu.init();

        driveTrain = new drive(hardwareMap, telemetry);
        driveTrain.init();

        // Wait for the game to start (Display Gyro value while waiting)
        waitForStart();

        driveForward(12, 1);

        telemetry.addData("Autonomous", " Complete");
        telemetry.update();
        sleep(1000);  // Pause to display last telemetry message.
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
            telemetry.addData("CPI", driveTrain.COUNTS_PER_INCH);
            telemetry.addData("FLD", frontLeftDistance);
            telemetry.update();
            int frontLeftCounts = (int)(frontLeftDistance * driveTrain.COUNTS_PER_INCH);
            int frontRightCounts = (int)(frontRightDistance * driveTrain.COUNTS_PER_INCH);
            int backLeftCounts = (int)(backLeftDistance * driveTrain.COUNTS_PER_INCH);
            int backRightCounts = (int)(backRightDistance * driveTrain.COUNTS_PER_INCH);

            int frontLeftTarget = driveTrain.frontLeft.getCurrentPosition()+frontLeftCounts;
            int frontRightTarget = driveTrain.frontRight.getCurrentPosition()+frontRightCounts;
            int backLeftTarget = driveTrain.backLeft.getCurrentPosition()+backLeftCounts;
            int backRightTarget = driveTrain.backRight.getCurrentPosition()+backRightCounts;

            // Determine new target position, and pass to motor controller
            // Set Target FIRST, then turn on RUN_TO_POSITION
            driveTrain.frontLeft.setTargetPosition(driveTrain.frontLeft.getCurrentPosition()+frontLeftTarget);
            driveTrain.frontRight.setTargetPosition(driveTrain.frontRight.getCurrentPosition()+frontRightTarget);
            driveTrain.backLeft.setTargetPosition(driveTrain.backLeft.getCurrentPosition()+backLeftTarget);
            driveTrain.backRight.setTargetPosition(driveTrain.backRight.getCurrentPosition()+backRightTarget);

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

            // keep looping while we are still active, and BOTH motors are running.
            while (opModeIsActive() &&
                    (driveTrain.frontLeft.isBusy() && driveTrain.frontRight.isBusy())&&
                    driveTrain.backLeft.isBusy()){ //&& driveTrain.backRight.isBusy()) {
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
