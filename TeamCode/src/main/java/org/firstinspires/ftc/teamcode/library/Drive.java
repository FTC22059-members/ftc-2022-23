package org.firstinspires.ftc.teamcode.library;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.library.Imu;

public class Drive {

    private HardwareMap hardwareMap;
    private Telemetry telemetry;
    private Imu imu;

    private double robotHeading = 0;
    private double headingOffset = 0;
    private double headingError = 0;

    // These variable are declared here (as class members) so they can be updated in various methods,
    // but still be displayed by sendTelemetry()
    private double targetHeading = 0;
    private double driveSpeed = 0;
    private double turnSpeed = 0;
    private double leftX = 0;
    private double rightSpeed = 0;
    private int leftTarget = 0;
    private int rightTarget = 0;

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

    // These constants define the desired driving/control characteristics
    // They can/should be tweaked to suit the specific robot drive train.
    static final double DRIVE_SPEED = 0.4;     // Max driving speed for better distance accuracy.
    static final double TURN_SPEED = 0.2;     // Max Turn speed to limit turn rate
    static final double HEADING_THRESHOLD = 1.0;    // How close must the heading get to the target before moving to next step.
    // Requiring more accuracy (a smaller number) will often make the turn take longer to get into the final position.
    // Define the Proportional control coefficient (or GAIN) for "heading control".
    // We define one value when Turning (larger errors), and the other is used when Driving straight (smaller errors).
    // Increase these numbers if the heading does not corrects strongly enough (eg: a heavy robot or using tracks)
    // Decrease these numbers if the heading does not settle on the correct value (eg: very agile robot with omni wheels)
    static final double P_TURN_GAIN = 0.02;     // Larger is more responsive, but also less stable
    static final double P_DRIVE_GAIN = 0.03;     // Larger is more responsive, but also less stable
    /**
     * TODO: JavaDOC
     */
    public Drive(HardwareMap hardwareMapCon, Telemetry telemetryCon, Imu imu) {
        this.hardwareMap = hardwareMapCon;
        this.telemetry = telemetryCon;
        this.imu = imu;
    }
    /**
     * TODO: JavaDOC
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
     * @param speedMultiplier        multiplier used to slow down the drive speed, used in precision mode
     * @param accelerationMultiplier multiplier used to change ramping behavior
     */
    public void moveRobot(double joystickMagnitude, double joystickAngle, double turnRate,
                          double speedMultiplier, double accelerationMultiplier) {

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
        backLeft.setPower(backLeftPower * speedMultiplier * accelerationMultiplier);
        backRight.setPower(backRightPower * speedMultiplier * accelerationMultiplier);
        frontLeft.setPower(frontLeftPower * speedMultiplier * accelerationMultiplier);
        frontRight.setPower(frontRightPower * speedMultiplier * accelerationMultiplier);
    }

    /**
     * This method takes separate drive (aka magnitude, fwd/rev) and turn (aka turn, right/left) requests
     * combines them, and applies the appropriate speed commands to the mechanum drive.
     *
     * @param joystickMagnitude forward motor speed
     * @param joystickAngle     clockwise turning motor speed.
     */
    public void moveRobot(double joystickMagnitude, double joystickAngle) {
        this.moveRobot(joystickMagnitude, joystickAngle, 0, 1, 1);
    }
    /**
     * TODO: JavaDOC
     */
    public void stop() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }

    public double getCountsPerInch() {
        return COUNTS_PER_INCH;
    }
    // TODO: JAVADOC ME!!!!!!
    // positive inputDegrees turns counterclockwise
    // negative inputDegrees turns clockwise

    public void turn(double inputDegrees) {
        double imuAngle = imu.getAngle();
        double desiredAngle = imuAngle + inputDegrees;
        if (inputDegrees > 0) {
            while (imu.getAngle() < desiredAngle) {
                moveRobot(0, 0, -0.5, 1, 1);
                //telemetry.addData("currentAngle", imu.getAngle());
                //telemetry.addData("desiredAngle", desiredAngle);
                //telemetry.update();
            }
        } else if (inputDegrees < 0) {
            while (imu.getAngle() > desiredAngle) {
                moveRobot(0, 0, 0.5, 1, 1);
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
     * TODO: JavaDOC
     */
    public void snapCw() {
        double imuAngle = imu.getAngle();
        if (imuAngle > 0 && imuAngle < 90) {
            turn(0 - imuAngle);
        } else if (imuAngle > 90 && imuAngle < 180) {
            turn(90 - imuAngle);
        } else if (imuAngle > -180 && imuAngle < -90) {
            turn(-180 - imuAngle);
        } else {
            turn(-90 - imuAngle);
        }
    }

    /**
     * TODO: JavaDOC
     */
    public void snapCcw() {
        double imuAngle = imu.getAngle();
        if (imuAngle > 0 && imuAngle < 90) {
            turn(90 - imuAngle);
        } else if (imuAngle > 90 && imuAngle < 180) {
            turn(180 - imuAngle);
        } else if (imuAngle > -180 && imuAngle < -90) {
            turn(-90 - imuAngle);
        } else {
            turn(0 - imuAngle);
        }
    }
    /**
     * This is a NOT WORKING concept function for snapping according to an algorithim
     */
    public void snapAlgo(   ) {
        int angle = 2;
        turn((90 * Math.floor(angle / 90)) - angle);
    }

    /**
     * TODO: JavaDOC
     */

    public void forwardsTime(double time, double direction) {
        ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        timer.reset();
        while (timer.milliseconds() / 1000 < time) {
            moveRobot(1, direction);
        }
    }
    /**
     * TODO: JavaDOC
     */
    public void forwardsTime(double time) {
        ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        timer.reset();
        while (timer.milliseconds() / 1000 < time) {
            moveRobot(1, 0);
        }
    }
    /**
     * TODO: JavaDOC
     */
    public void backwardsTime(double time, double direction) {
        ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        timer.reset();
        while (timer.milliseconds() / 1000 < time) {
            moveRobot(1, -direction);
        }
    }
    /**
     * TODO: JavaDOC
     */
    public void backwardsTime(double time) {
        ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        timer.reset();
        while (timer.milliseconds() / 1000 < time) {
            moveRobot(1, 180);
        }
    }
}