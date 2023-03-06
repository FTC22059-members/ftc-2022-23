package org.firstinspires.ftc.teamcode.library;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public class Imu {

    private HardwareMap hardwareMap;

    private IMU imu;
    private double lastYaw = 0.0;
    private double globalAngle;


    public Imu(HardwareMap hardwareMapCon) {
        this.hardwareMap = hardwareMapCon;
    }

    /**
     * Resets the angle for the imu so that it's accurate to whatever position
     */
    public void resetAngle() {
        imu.resetYaw();
        lastYaw = 0.0;
        globalAngle = 0.0;
    }

    /**
     * Get current cumulative angle rotation from last reset.
     *
     * @return Angle in degrees. + = left, - = right.
     */
    public double getAngle() {
        // We experimentally determined the Z axis is the axis we want to use for heading angle.
        // We have to process the angle because the imu works in euler angles so the Z axis is
        // returned as 0 to +180 or 0 to -180 rolling back to -179 or +179 when rotation passes
        // 180 degrees. We detect this transition and track the total cumulative angle of rotation.

        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();

        double deltaAngle = orientation.getYaw(AngleUnit.DEGREES) - lastYaw;

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        globalAngle += deltaAngle;

        lastYaw = orientation.getYaw(AngleUnit.DEGREES);

        return globalAngle;
    }

    public double getAngleRadians() {
        // convert degrees to radians
        return (Math.PI * this.getAngle()) / 180;
    }

    public double checkDirection() {
        // The gain value determines how sensitive the correction is to direction changes.
        // You will have to experiment with your robot to get small smooth direction changes
        // to stay on a straight line.
        double correction, angle, gain = .10;

        angle = getAngle();

        if (angle == 0)
            correction = 0;             // no adjustment.
        else
            correction = -angle;        // reverse sign of angle for correction.

        correction = correction * gain;

        return correction;
    }


    public boolean init() {
        imu = hardwareMap.get(IMU.class, "imuCH");

        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.RIGHT;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection = RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;

        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);

        boolean initSuccessful = imu.initialize(new IMU.Parameters(orientationOnRobot));

        return initSuccessful;
    }

    public void preLoop() {

    }

    public void imuLoop() {

    }

}