package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class PID {
    private HardwareMap hwMap;
    private Telemetry telemetry;

    public PID(HardwareMap hwMapCon, Telemetry telemetryCon){
        this.hwMap=hwMapCon;
        this.telemetry=telemetryCon;
    }
}
