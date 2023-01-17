package org.firstinspires.ftc.teamcode.library;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class FancyTelemetry {
    private Telemetry telemetry;

    public FancyTelemetry(Telemetry telemetry){
        this.telemetry = telemetry;
        this.telemetry.setCaptionValueSeparator("  ");
        this.telemetry.setItemSeparator(", ");
//        this.telemetry.setDisplayFormat(Telemetry.DisplayFormat.HTML);
    }

    public void addBool(String label, Boolean value) {
        String box = "☐";
        if (value) {
            box = "☒";
        }
        this.telemetry.addLine().addData(box, label);
    }

    public void addNumber(String label, double value, double min, double max) {

    }

    public void addNumber(String label, double value) {
        this.telemetry.addData(label, value);
    }

    public void addText(String label, String value) {
        this.telemetry.addData(label, value);
    }

    public void loop(){
        this.telemetry.update();
    }
}
