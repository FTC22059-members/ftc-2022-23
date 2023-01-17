package org.firstinspires.ftc.teamcode.library.demos;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.library.Arm;
import org.firstinspires.ftc.teamcode.library.FancyTelemetry;

@TeleOp(name = "Test Telemetry")
public class TelemetryDemo extends LinearOpMode {

    @Override
    public void runOpMode() {
        FancyTelemetry fancyTelemetry = new FancyTelemetry(telemetry);

        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            fancyTelemetry.addBool("Text Bool (f)", false);
            fancyTelemetry.addText("Type", "String");
            fancyTelemetry.addBool("Text Bool (t)", true);
            fancyTelemetry.addNumber("Number",1234);

            fancyTelemetry.loop();
        }
    }
}
