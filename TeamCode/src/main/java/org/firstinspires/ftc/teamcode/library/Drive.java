package org.firstinspires.ftc.teamcode.library;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Drive {

    private HardwareMap hardwareMap;
    private Telemetry telemetry;

    private double          robotHeading  = 0;
    private double          headingOffset = 0;
    private double          headingError  = 0;

    // These variable are declared here (as class members) so they can be updated in various methods,
    // but still be displayed by sendTelemetry()
    private double  targetHeading = 0;
    private double  driveSpeed    = 0;
    private double  turnSpeed     = 0;
    private double  leftX     = 0;
    private double  rightSpeed    = 0;
    private int     leftTarget    = 0;
    private int     rightTarget   = 0;

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
    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;   // Used here is a TorqueNado from Tetrix; look at spec sheet for yours.
    static final double     DRIVE_GEAR_REDUCTION    = 0.5 ;     // No External Gearing.
    static final double     WHEEL_DIAMETER_INCHES   = 3.858 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    // These constants define the desired driving/control characteristics
    // They can/should be tweaked to suit the specific robot drive train.
    static final double     DRIVE_SPEED             = 0.4;     // Max driving speed for better distance accuracy.
    static final double     TURN_SPEED              = 0.2;     // Max Turn speed to limit turn rate
    static final double     HEADING_THRESHOLD       = 1.0 ;    // How close must the heading get to the target before moving to next step.
    // Requiring more accuracy (a smaller number) will often make the turn take longer to get into the final position.
    // Define the Proportional control coefficient (or GAIN) for "heading control".
    // We define one value when Turning (larger errors), and the other is used when Driving straight (smaller errors).
    // Increase these numbers if the heading does not corrects strongly enough (eg: a heavy robot or using tracks)
    // Decrease these numbers if the heading does not settle on the correct value (eg: very agile robot with omni wheels)
    static final double     P_TURN_GAIN            = 0.02;     // Larger is more responsive, but also less stable
    static final double     P_DRIVE_GAIN           = 0.03;     // Larger is more responsive, but also less stable

    public Drive(HardwareMap hardwareMapCon, Telemetry telemetryCon) {
        this.hardwareMap = hardwareMapCon;
        this.telemetry = telemetryCon;
    }

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

        //this may be a prime suspect for errors, this was in pre-loop, didn't make sense to me - Zach
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /**
     * This method uses a Proportional Controller to determine how much steering correction is required.
     *
     * @param desiredHeading        The desired absolute heading (relative to last heading reset)
     * @param proportionalGain      Gain factor applied to heading error to obtain turning power.
     * @return                      Turning power needed to get to required heading.
     */

/**    public double getSteeringCorrection(double desiredHeading, double proportionalGain) {
        targetHeading = desiredHeading;  // Save for telemetry

        // Get the robot heading by applying an offset to the IMU heading
        robotHeading = getRawHeading() - headingOffset;

        // Determine the heading current error
        headingError = targetHeading - robotHeading;

        // Normalize the error to be within +/- 180 degrees
        while (headingError > 180)  headingError -= 360;
        while (headingError <= -180) headingError += 360;

        // Multiply the error by the gain to determine the required steering correction/  Limit the result to +/- 1.0
        return Range.clip(headingError * proportionalGain, -1, 1);
    }**/

    /**
     * This method takes separate drive (aka magnitude, fwd/rev) and turn (aka turn, right/left) requests
     * as well as turn rate and multipliers, combines them, and applies the appropriate speed commands to the mechanum drive.
     * @param joystickMagnitude forward motor speed
     * @param joystickAngle  clockwise turning motor speed.
     * @param turnRate turning rate (like a joystick)
     * @param speedMultiplier  multiplier used to slow down the drive speed, used in precision mode
     * @param accelerationMultiplier multiplier used to change ramping behavior
     */
    public void moveRobot(double joystickMagnitude, double joystickAngle, double turnRate,
                          double speedMultiplier, double accelerationMultiplier) {
        
        double leftX = joystickMagnitude * sin(joystickAngle);
        double leftY = joystickMagnitude * cos(joystickAngle);

        telemetry.addData("newx", leftX);
        telemetry.addData("newy", leftY);
        telemetry.addData("turnRate", turnRate);
        telemetry.addData("Angle (Radians)", joystickAngle);

        // figure out the power for each wheel
        double denominator = Math.max(Math.abs(leftY) + Math.abs(leftX) + Math.abs(turnRate), 1);
        double frontLeftPower = (leftY + leftX + turnRate) / denominator;
        double backLeftPower = (leftY - leftX + turnRate) / denominator;
        double frontRightPower = (leftY - leftX - turnRate) / denominator;
        double backRightPower = (leftY + leftX - turnRate) / denominator;

        //accelerationMultiplier=Math.pow(Math.abs(frontLeftPower), 2.5-gamepad1.left_trigger*1.5);

        // actually tell the wheels to move! (finally)
        backLeft.setPower(backLeftPower * speedMultiplier * accelerationMultiplier);
        backRight.setPower(backRightPower * speedMultiplier * accelerationMultiplier);
        frontLeft.setPower(frontLeftPower * speedMultiplier * accelerationMultiplier);
        frontRight.setPower(frontRightPower * speedMultiplier * accelerationMultiplier);
    }

    /**
     * This method takes separate drive (aka magnitude, fwd/rev) and turn (aka turn, right/left) requests
     * combines them, and applies the appropriate speed commands to the mechanum drive.
     * @param joystickMagnitude forward motor speed
     * @param joystickAngle  clockwise turning motor speed.
     */
    public void moveRobot(double joystickMagnitude, double joystickAngle) {
        this.moveRobot(joystickMagnitude, joystickAngle,0,1,1);
    }
    /**
     *  Display the various control parameters while driving
     *
     * @param straight  Set to true if we are driving straight, and the encoder positions should be included in the telemetry.

    private void sendTelemetry(boolean straight) {

        if (straight) {
            telemetry.addData("Motion", "Drive Straight");
            telemetry.addData("Target Pos L:R",  "%7d:%7d",      leftTarget,  rightTarget);
            telemetry.addData("Actual Pos L:R",  "%7d:%7d",      leftDrive.getCurrentPosition(),
                    rightDrive.getCurrentPosition());
        } else {
            telemetry.addData("Motion", "Turning");
        }

        telemetry.addData("Angle Target:Current", "%5.2f:%5.0f", targetHeading, robotHeading);
        telemetry.addData("Error:Steer",  "%5.1f:%5.1f", headingError, turnSpeed);
        telemetry.addData("Wheel Speeds L:R.", "%5.2f : %5.2f", leftX, rightSpeed);
        telemetry.update();
    }*/

    public double getCountsPerInch(){
        return COUNTS_PER_INCH;
    }
}
