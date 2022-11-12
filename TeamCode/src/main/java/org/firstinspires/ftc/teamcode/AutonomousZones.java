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
        telemetry.addLine("Zone = A");
        telemetry.update();
    }
    public void doZoneB(){
        telemetry.addLine("Zone = B");
        telemetry.update();
    }
    public void doZoneC(){
        telemetry.addLine("Zone = C");
        telemetry.update();
    }
}
