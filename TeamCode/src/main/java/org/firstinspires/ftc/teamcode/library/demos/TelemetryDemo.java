package org.firstinspires.ftc.teamcode.library.demos;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.library.fancyTelemetry.Surface;
import org.firstinspires.ftc.teamcode.library.fancyTelemetry.Orientations;
import org.firstinspires.ftc.teamcode.library.fancyTelemetry.Renderer;

@TeleOp(name = "Test Telemetry")
public class TelemetryDemo extends LinearOpMode {

    @Override
    public void runOpMode() {
        Renderer fancyTelemetry = new Renderer(telemetry);

        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
             Surface ctx = new Surface(30, 10)
                     .drawText(0, 0, "Testing 1 2 3")
                    .drawGauge(0, 1, 30, 10, -2, 40, Orientations.VERTICAL)
                    .drawLineH(0, 2, 30)
                    .drawCheckbox(1, 3, "Global Positioning", true)
                    .drawCheckbox(1, 4, "Precision Mode", false);
             fancyTelemetry.insert(ctx, 0, 0, false, false, false);
            fancyTelemetry.loop();
        }
    }
}
