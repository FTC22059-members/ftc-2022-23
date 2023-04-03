package com.overclockedftc.typographer;

/*
 * # Conventions
 * ## Code
 * All drawing methods (draw*) have a public version and a private version. The private version allows you to ignore padding, the public one doesn't have that option. The private one should come first.
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
 * Renders the root Surface and displays it to telemetry.
 */
public class Renderer {
    private Telemetry telemetry;
    private int width = 34;
    private int height = 16;


    private Texel[][] buffer;

    public Renderer(Telemetry telemetry) {
        this.buffer = new Texel[this.height][this.width];
        this.telemetry = telemetry;
        this.telemetry.setCaptionValueSeparator("  ");
        this.telemetry.setItemSeparator(", ");
        this.telemetry.setDisplayFormat(Telemetry.DisplayFormat.MONOSPACE);
    }

    public void loop() {
        String string = "";
        for (int y = 0; y < this.buffer.length; y++) {
            String row = "";
            for (int x = 0; x < this.buffer[y].length; x++) {
                Texel texel = this.buffer[y][x];
                if (texel == null) {
                    row += new SpacedChar(new Texel().render()).render();
                } else if (texel.isCommand) {
                    row += new SpacedChar(texel.command.render()).render();
                } else {
                    row += new SpacedChar(texel.content).render();
                }
            }
            string += row + "\n";
            // workspace.innerHTML = string;
        }
        this.telemetry.addLine(string);

        this.telemetry.update();

        this.buffer = new Texel[this.height][this.width];
    }

    public void insert(Surface context, int x, int y, boolean showBoxShadow, boolean showBorder,
                       boolean showName) {
        Texel[][] buff = context.render();
        int paddingTop = 0;
        int paddingBottom = 0;
        int paddingLeft = 0;
        int paddingRight = 0;

        if (showBorder) {
            paddingTop = 1;
            paddingBottom = 1;
            paddingLeft = 1;
            paddingRight = 1;
        }

        if (showBoxShadow) {
            paddingBottom = 1;
            paddingRight = 1;
        }

        if (showName) {
            paddingTop = 1;
        }

        for (int cy = 0; cy < context.h; cy++) {
            for (int cx = 0; cx < context.w; cx++) {
                if (buff[cy][cx] != null) {
                    this.setChar(x + cx + paddingLeft, y + cy + paddingTop, buff[cy][cx]);
                }
            }
        }
        if (showBorder) {
            this.drawRect(x, y, context.w + paddingTop + paddingBottom, context.h + paddingLeft + paddingRight);
        }

        if (showName && context.name != null && context.w - 3 > 0) {
            if (showBorder) {
                this.drawText(x + 2, y, context.name, context.w - 1);
            } else {
                this.drawText(x, y, context.name, context.w - 1);
            }
        }

        if (showBoxShadow) {
            this.drawBoxShadow(x, y, context.w + paddingTop + paddingBottom, context.h + paddingLeft + paddingRight);
        }
    }

    public void drawRect(int x, int y, int w, int h) {
        w -= 1;
        h -= 1;

        this.setChar(x, y, new Texel(new Frame(new int[] { 0, 1, 0, 1 })));
        this.setChar(x + w, y, new Texel(new Frame(new int[] { 0, 1, 1, 0 })));
        this.setChar(x, y + h, new Texel(new Frame(new int[] { 1, 0, 0, 1 })));
        this.setChar(x + w, y + h, new Texel(new Frame(new int[] { 1, 0, 1, 0 })));

        for (int i = 1; i < h; i++) {
            this.setChar(x, y + i, new Texel(new Frame(new int[] { 1, 1, 0, 0 })));
            this.setChar(x + w, y + i, new Texel(new Frame(new int[] { 1, 1, 0, 0 })));
        }

        for (int i = 1; i < w; i++) {
            this.setChar(x + i, y, new Texel(new Frame(new int[] { 0, 0, 1, 1 })));
            this.setChar(x + i, y + h, new Texel(new Frame(new int[] { 0, 0, 1, 1 })));
        }
    }

    private void setChar(int x, int y, Texel val) {
        if (0 <= x && x < this.width && 0 <= y && y < this.height) {
            this.buffer[y][x] = val.underlay(this.buffer[y][x], false);
        }
    }

    public void drawBoxShadow(int x, int y, int w, int h) {
        w -= 1;
        h -= 1;

        this.setChar(x + w, y + h, new Texel('\u259F'));

        for (int i = 1; i < h; i++) {
            this.setChar(x + w, y + i, new Texel('\u2590'));
        }

        for (int i = 1; i < w; i++) {
            this.setChar(x + i, y + h, new Texel(new Fill(4, Orientations.VERTICAL)));
        }
    }

    private void drawText(int x, int y, String text, int width) {
        int col = 0;
        for (int i = 0; i < text.length(); i++) {
            char chunk = text.charAt(i);

            if (chunk == '\n') {
                break;
            } else {
                if (col >= width) {
                    break;
                }
                this.setChar(x + col, y, new Texel(chunk));
                col++;
            }
        }
    }
}
