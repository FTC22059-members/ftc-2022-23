package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;

public class AutonomousZones {
    private HardwareMap hwMap;
    private Telemetry telemetry;

    public AutonomousZones(HardwareMap hwMapCon, Telemetry telemetryCon){
        this.hwMap=hwMapCon;
        this.telemetry=telemetryCon;
    }

    public void doZoneA(){

    }
    public void doZoneB(){

    }
    public void doZoneC(){

    }
}
