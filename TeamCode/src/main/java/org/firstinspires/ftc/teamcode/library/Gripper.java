package org.firstinspires.ftc.teamcode.library;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

// flat gripper (open) = -66
// closed = -96
// their 90 is our 0
public class Gripper {
    private Servo gripperServo;

    private Gamepad gamepad1;
    private Gamepad gamepad2;
    private HardwareMap hardwareMap;
    private Telemetry telemetry;

    private static double OPENANGLE = 0.125;
    private static double CLOSEANGLE = 0.48;
    
    private static boolean ISOPEN = true;

    /**
     * Constructor for the gripper
     * @param hardwareMapImport The hardware map to be used in gripper
     * @param telemetryImport The telemetry to be used in gripper
     */
    public Gripper(HardwareMap hardwareMapImport, Telemetry telemetryImport) {
        this.hardwareMap = hardwareMapImport;
        this.telemetry = telemetryImport;
    }

    /**
     * Initalization method for the gripper, which imports the gamepads.
     * @param gamepad1 Imports gamepad 1
     * @param gamepad2 Imports gamepad 2
     */
    public void init(Gamepad gamepad1, Gamepad gamepad2) {
        gripperServo = hardwareMap.get(Servo.class, "gripperMotor");
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;

    }

    /**
     * Pre-loop code for gripper. Currently just opens the gripper.
     */
    public void preLoop() {
        open();
    }

    /**
     * Code to run the gripper's loop. Contains code to open and close the gripper
     */
    public void gripperLoop() {
        if (gamepad2.right_trigger > 0 && ISOPEN) {
            close();
            ISOPEN = false;
        } else if (gamepad2.left_trigger > 0 && !ISOPEN) {
            open();
            ISOPEN = true;
        }
        telemetry.addData("Gripper Open?", ISOPEN);
    }

    /**
     * Opens gripper
     */
    public void open() {
        if (gripperServo.getPosition() != OPENANGLE) {
            gripperServo.setPosition(OPENANGLE);
        }
    }

    /**
     * Closes gripper
     */
    public void close() {
        if (gripperServo.getPosition() != CLOSEANGLE) {
            gripperServo.setPosition(CLOSEANGLE);
        }
    }

    /**
     * Gets whether the gripper is open
     * @return Returns whether the gripper is open (TRUE=OPEN)
     */
    public boolean getIsOpen() {
        return ISOPEN;
    }
}