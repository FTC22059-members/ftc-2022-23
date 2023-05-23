package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Global {
    private final Gamepad gamepad2;
    private final Gamepad gamepad1;
    public HardwareMap hardwareMap;
    public Telemetry telemetry;

    public Global(HardwareMap robotHardwareMap, Telemetry robotTelemetry, Gamepad gamepad1, Gamepad gamepad2) {
        this.hardwareMap = robotHardwareMap;
        this.telemetry = robotTelemetry;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }

}
