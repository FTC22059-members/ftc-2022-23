package org.firstinspires.ftc.teamcode.library.fancyTelemetry;

/*
 * # Conventions
 * ## Code
 *
 * ## Javadoc
 * Use <b></b> tags for class names, and DEFINITELY for the name of the class you are javadoc-ing.
 * Use <i></i> tags for properties and methods.
 * Use <code></code> tags for variable names, values, or keywords.
 * When referencing methods, always end it with parentheses like myMethod()
 * Capitalize Texels.
 * Don't finish line of a Javadoc with a period unless there is more than one sentence.
 */

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Renders the root Graphics Context and displays it to telemetry.
 */
public class RenderTelemetry {
    private Telemetry telemetry;
    public GraphicsContext display;
    private int WIDTH = 30;
    private int HEIGHT = 10;

    public RenderTelemetry(Telemetry telemetry) {
        this.display = new GraphicsContext(this.WIDTH, this.HEIGHT);
        this.telemetry = telemetry;
        this.telemetry.setCaptionValueSeparator("  ");
        this.telemetry.setItemSeparator(", ");
        this.telemetry.setDisplayFormat(Telemetry.DisplayFormat.MONOSPACE);
    }

    public void loop() {
        Texel[][] buffer = display.render();

        String string = "";
        for (int y = 0; y < buffer.length; y++) {
            String row = "";
            for (int x = 0; x < buffer[y].length; x++) {
                Texel texel = buffer[y][x];
                if (texel == null) {
                    row += " ";
                } else if (texel.isCommand) {
                    row += texel.command.render();
                } else {
                    row += texel.content;
                }
            }
            string += row + "\n";
            // workspace.innerHTML = string;
        }
        this.telemetry.addData("", string);

        this.telemetry.update();

        this.display.clearAll();
    }
}
