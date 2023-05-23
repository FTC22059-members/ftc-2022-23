package org.firstinspires.ftc.teamcode.modules;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Global;

public class Drive {

    private HardwareMap hardwareMap;
    private Telemetry telemetry;
    private Imu imu;

    //Initialize motor variables
    public DcMotorEx backLeft;
    public DcMotorEx backRight;
    public DcMotorEx frontLeft;
    public DcMotorEx frontRight;

    // Calculate the COUNTS_PER_INCH for your specific drive train.
    // Go to your motor vendor website to determine your motor's COUNTS_PER_MOTOR_REV
    // For external drive gearing, set DRIVE_GEAR_REDUCTION as needed.
    // For example, use a value of 2.0 for a 12-tooth spur gear driving a 24-tooth spur gear.
    // This is gearing DOWN for less speed and more torque.
    // For gearing UP, use a gear ratio less than 1.0. Note this will affect the direction of wheel rotation.
    static final double COUNTS_PER_MOTOR_REV = 1440;   // Used here is a TorqueNado from Tetrix; look at spec sheet for yours.
    static final double DRIVE_GEAR_REDUCTION = 0.5;     // No External Gearing.
    static final double WHEEL_DIAMETER_INCHES = 3.858;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double SPEED_LIMIT = 0.83;

    /**
     * Constructor for Drive
     * @param global
     * @param imu
     */
    public Drive(Global global, Imu imu) {
        this.hardwareMap = global.hardwareMap;
        this.telemetry = global.telemetry;
        this.imu = imu;
    }
    /**
     * Initiates drive, sets up motors
     */
    public void init() {
        //Find motors in hardware map (in the driver station).
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");

        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /**
     * This method takes separate drive (aka magnitude, fwd/rev) and turn (aka turn, right/left) requests
     * as well as turn rate and multipliers, combines them, and applies the appropriate speed commands to the mechanum drive.
     *
     * @param joystickMagnitude      forward motor speed
     * @param joystickAngle          clockwise turning motor speed.
     * @param turnRate               turning rate (like a joystick)
     * @param brakeMultiplier        multiplier used to slow down the drive speed, used in precision mode
     */
    public void moveRobot(double joystickMagnitude, double joystickAngle, double turnRate,
                          double brakeMultiplier) {

        double leftX = joystickMagnitude * sin(joystickAngle);
        double leftY = joystickMagnitude * cos(joystickAngle);

        //telemetry.addData("newx", leftX);
        //telemetry.addData("newy", leftY);
        //telemetry.addData("turnRate", turnRate);
        //telemetry.addData("Angle (Radians)", joystickAngle);

        // figure out the power for each wheel
        double denominator = Math.max(Math.abs(leftY) + Math.abs(leftX) + Math.abs(turnRate), 1);
        double frontLeftPower = (leftY + leftX + turnRate) / denominator;
        double backLeftPower = (leftY - leftX + turnRate) / denominator;
        double frontRightPower = (leftY - leftX - turnRate) / denominator;
        double backRightPower = (leftY + leftX - turnRate) / denominator;

        // actually tell the wheels to move! (finally)
        backLeft.setPower(backLeftPower * brakeMultiplier * SPEED_LIMIT);
        backRight.setPower(backRightPower * brakeMultiplier * SPEED_LIMIT);
        frontLeft.setPower(frontLeftPower * brakeMultiplier * SPEED_LIMIT);
        frontRight.setPower(frontRightPower * brakeMultiplier * SPEED_LIMIT);
    }

    /**
     * This method takes separate drive (aka magnitude, fwd/rev) and turn (aka turn, right/left) requests
     * combines them, and applies the appropriate speed commands to the mechanum drive.
     *
     * @param joystickMagnitude forward motor speed
     * @param joystickAngle     clockwise turning motor speed.
     */
    public void moveRobot(double joystickMagnitude, double joystickAngle) {
        this.moveRobot(joystickMagnitude, joystickAngle, 0, 1);
    }

    /**
     * Stops the robot
     */
    public void stop() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }

    /**
     *
     * @return COUNTS_PER_INCH, positive is counterclockwise and negative is clockwise
     */
    public double getCountsPerInch() {
        return COUNTS_PER_INCH;
    }

    /**
     * Turns the robot at a set speed
     * @param inputDegrees
     */
    public void turn(double inputDegrees) {
        double imuAngle = imu.getAngle();
        double desiredAngle = imuAngle + inputDegrees;

        // turn's timeout
        ElapsedTime timer = new ElapsedTime(0);
        timer.reset();
        if (inputDegrees > 0) {
            while (imu.getAngle() < desiredAngle && timer.seconds() < 1.5) {
                moveRobot(0, 0, -0.5, 1);
                //telemetry.addData("currentAngle", imu.getAngle());
                //telemetry.addData("desiredAngle", desiredAngle);
                //telemetry.update();
            }
        } else if (inputDegrees < 0) {
            while (imu.getAngle() > desiredAngle && timer.seconds() < 1.5) {
                moveRobot(0, 0, 0.5, 1);
                //telemetry.addData("currentAngle", imu.getAngle());
                //telemetry.addData("desiredAngle", desiredAngle);
                //telemetry.update();
            }
        } else {
            //telemetry.addLine("bruh");
        }
        stop();

        //telemetry.update();
    }

    /**
     * Snaps to nearest 90 degree interval clockwise
     */
    public void snapCw() {
        double imuAngle = imu.getAngle();
        turn((90 * Math.floor(imuAngle / 90)) - imuAngle);
    }

    /**
     * Snaps to nearest 90 degree interval counterclockwise
     */
    public void snapCcw() {
        double imuAngle = imu.getAngle();
        turn((90 * Math.ceil(imuAngle / 90)) - imuAngle);
    }

    /**
     * Moves the robot forward for an amount of time and in a direction
     * @param time
     * @param direction
     */
    public void forwardsTime(double time, double direction) {
        ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        timer.reset();
        while (timer.milliseconds() / 1000 < time) {
            moveRobot(1, direction);
        }
    }

    /**
     * Moves the robot forward for an amount of time
     * @param time
     */
    public void forwardsTime(double time) {
        ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        timer.reset();
        while (timer.milliseconds() / 1000 < time) {
            moveRobot(1, 0);
        }
    }

    /**
     * Moves the robot backward for an amount of time and in a direction
     * @param time
     * @param direction
     */
    public void backwardsTime(double time, double direction) {
        ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        timer.reset();
        while (timer.milliseconds() / 1000 < time) {
            moveRobot(1, -direction);
        }
    }

    /**
     * Moves the robot backward for an amount of time
     * @param time
     */
    public void backwardsTime(double time) {
        ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        timer.reset();
        while (timer.milliseconds() / 1000 < time) {
            moveRobot(1, 180);
        }
    }
}