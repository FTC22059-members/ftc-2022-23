package org.firstinspires.ftc.teamcode.library;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;

public class AprilTags {

    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;

    static final double FEET_PER_METER = 3.28084;

    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the C920 webcam at 800x448.
    // You will need to do your own calibration for other configurations!
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    // UNITS ARE METERS
    double tagsize = 0.166;

    int numFramesWithoutDetection = 0;

    final float DECIMATION_HIGH = 3;
    final float DECIMATION_LOW = 2;
    final float THRESHOLD_HIGH_DECIMATION_RANGE_METERS = 1.0f;
    final int THRESHOLD_NUM_FRAMES_NO_DETECTION_BEFORE_LOW_DECIMATION = 4;
    private HardwareMap hwMap;
    private Telemetry telemetry;

    /**
     * Construction for AprilTags
     * @param hwMapCon
     * @param telemetryCon
     */
    public AprilTags(HardwareMap hwMapCon, Telemetry telemetryCon){
        this.hwMap=hwMapCon;
        this.telemetry=telemetryCon;
    }

    /**
     * Initiates AprilTags, starts camera
     */
    public void init(){
        int cameraMonitorViewId = this.hwMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", this.hwMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(this.hwMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(800,448, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        });
        this.detectTag();
    }

    /**
     * Sets how often telemetry sends data
     */
    public void preLoop(){
        this.telemetry.setMsTransmissionInterval(50);
    }

    /**
     * Detects a tag, if it does not detect a tag it lowers decimation to try to detect it
     * @return zoneString
     */
    public String detectTag(){

        String zoneString = "Not found";

        // Calling getDetectionsUpdate() will only return an object if there was a new frame
        // processed since the last time we called it. Otherwise, it will return null. This
        // enables us to only run logic when there has been a new frame, as opposed to the
        // getLatestDetections() method which will always return an object.
        ArrayList<AprilTagDetection> detections = aprilTagDetectionPipeline.getDetectionsUpdate();

        // If there's been a new frame...
        if(detections != null)
        {
            this.telemetry.addData("FPS", camera.getFps());
            this.telemetry.addData("Overhead ms", camera.getOverheadTimeMs());
            this.telemetry.addData("Pipeline ms", camera.getPipelineTimeMs());

            // If we don't see any tags

            if(detections.size() == 0)
            {
                numFramesWithoutDetection++;

                // If we haven't seen a tag for a few frames, lower the decimation
                // so we can hopefully pick one up if we're e.g. far back
                if(numFramesWithoutDetection >= THRESHOLD_NUM_FRAMES_NO_DETECTION_BEFORE_LOW_DECIMATION)
                {
                    aprilTagDetectionPipeline.setDecimation(DECIMATION_LOW);
                }
            }
            // We do see tags!
            else
            {
                numFramesWithoutDetection = 0;

                // If the target is within 1 meter, turn on high decimation to
                // increase the frame rate
                if(detections.get(0).pose.z < THRESHOLD_HIGH_DECIMATION_RANGE_METERS)
                {
                    aprilTagDetectionPipeline.setDecimation(DECIMATION_HIGH);
                }

                for(AprilTagDetection detection : detections)
                {
                    this.telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
                    this.telemetry.addLine(String.format("Translation X: %.2f feet", detection.pose.x*FEET_PER_METER));
                    this.telemetry.addLine(String.format("Translation Y: %.2f feet", detection.pose.y*FEET_PER_METER));
                    this.telemetry.addLine(String.format("Translation Z: %.2f feet", detection.pose.z*FEET_PER_METER));
                    this.telemetry.addLine(String.format("Rotation Yaw: %.2f degrees", Math.toDegrees(detection.pose.yaw)));
                    this.telemetry.addLine(String.format("Rotation Pitch: %.2f degrees", Math.toDegrees(detection.pose.pitch)));
                    this.telemetry.addLine(String.format("Rotation Roll: %.2f degrees", Math.toDegrees(detection.pose.roll)));

                    if (detection.id==13){
                        zoneString="A";
                    }else if (detection.id==14){
                        zoneString="B";
                    }else if (detection.id==15){
                        zoneString="C";
                    }else{
                        this.telemetry.addLine(String.format("\nINVALID TAG DETECTED=%d", detection.id));
                    }

                    this.telemetry.addData("Detected zone: ", zoneString);
                }
            }
            this.telemetry.update();
        }
        return zoneString;
        //sleep(20);
    }
}
