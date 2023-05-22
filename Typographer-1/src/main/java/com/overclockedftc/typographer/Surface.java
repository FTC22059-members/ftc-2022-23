package com.overclockedftc.typographer;

/**
 * A <b>Surface</b> represents a display. There is one root <b>Graphics
 * Context</b>, and it can have several sub <b>Surfaces</b> to
 * represent other windows.
 */
public class Surface {
    public int w;
    public int h;
    private Texel[][] buffer;
    private final int stroke = 1;
    public String name;

    /**
     * Initializes a <b>Surface</b> with a name.
     *
     * @param w    The width of the <b>Surface</b>
     * @param h    The height of the <b>Surface</b>
     * @param name The name of the <b>Surface</b>
     */
    public Surface(int w, int h, String name) {
        this.w = w;
        this.h = h;

        this.buffer = new Texel[h][w];

        this.name = name;
    }

    /**
     * Initializes a <b>Surface</b> without a name.
     *
     * @param w The width of the <b>Surface</b>
     * @param h The height of the <b>Surface</b>
     */

    public Surface(int w, int h) {
        this.w = w;
        this.h = h;

        this.buffer = new Texel[h][w];
    }

    //=====--------------------------=====//
    //=====----- Buffer Actions -----=====//
    //=====--------------------------=====//

    /**
     * Sets the value of a particular Texel
     *
     * @param x             The x coordinate of the Texel to set.
     * @param y             The x coordinate of the Texel to set.
     * @param val           The value to set it to.
     * @param sameLayer     Whether the Texels should be treated as though they were
     *                      on the same layer (allows merging or not).
     */
    public Surface setChar(int x, int y, Texel val, boolean sameLayer) {
        if (0 <= x && x < this.w && 0 <= y && y < this.h) {
            // this.buffer[y][x] = this.overlay(this.buffer[y][x], val, sameLayer);
            this.buffer[y][x] = val.underlay(this.buffer[y][x], sameLayer);
        }

        return this;
    }

    /**
     * Sets the value of a particular Texel with a shorthand for commands
     *
     * @param x         The x coordinate of the Texel to set.
     * @param y         The x coordinate of the Texel to set.
     * @param val       The command to set it to.
     * @param sameLayer Whether the Texels should be treated as though they were
     *                  on the same layer (allows merging or not).
     */
    public Surface setChar(int x, int y, Command val, boolean sameLayer) {
        if (0 <= x && x < this.w && 0 <= y && y < this.h) {
            // this.buffer[y][x] = this.overlay(this.buffer[y][x], val, sameLayer);
            this.buffer[y][x] = new Texel(val).underlay(this.buffer[y][x], sameLayer);
        }

        return this;
    }

    /**
     * Clears the <i>buffer</i>.
     */
    public void clear() {
        this.buffer = new Texel[h][w];
    }

    /**
     * Calls <i>render()</i> on all of it's children, flattening them with
     * <i>overlay()</i>, then returns the result. If it is a root <b>Graphics
     * Context</b>, it renders all of the commands and returns the result to get
     * sent to the <code>Driver Station</code>.
     *
     * @return
     */
    public Texel[][] render() {
        return this.buffer;
    }

    //=====-------------------=====//
    //=====----- Drawers -----=====//
    //=====-------------------=====//

    public Surface drawRect(int x, int y, int w, int h) {
        w -= 1;
        h -= 1;

        this.setChar(x, y, new Texel(new Frame(new int[]{0, this.stroke, 0, this.stroke})), true);
        this.setChar(x + w, y, new Texel(new Frame(new int[]{0, this.stroke, this.stroke, 0})), true);
        this.setChar(x, y + h, new Texel(new Frame(new int[]{this.stroke, 0, 0, this.stroke})), true);
        this.setChar(x + w, y + h, new Texel(new Frame(new int[]{this.stroke, 0, this.stroke, 0})), true);

        for (int i = 1; i < h; i++) {
            this.setChar(x, y + i, new Texel(new Frame(new int[]{this.stroke, this.stroke, 0, 0})), true);
            this.setChar(x + w, y + i, new Texel(new Frame(new int[]{this.stroke, this.stroke, 0, 0})), true);
        }

        for (int i = 1; i < w; i++) {
            this.setChar(x + i, y, new Texel(new Frame(new int[]{0, 0, this.stroke, this.stroke})), true);
            this.setChar(x + i, y + h, new Texel(new Frame(new int[]{0, 0, this.stroke, this.stroke})), true);
        }

        return this;
    }
    public Surface drawBoxShadow(int x, int y, int w, int h) {

            this.setChar(x + w, y + h, new Texel('\u259F'), false);

            for (int i = 1; i < h; i++) {
                this.setChar(x + w, y + i, new Texel('\u2590'), false);
            }

            for (int i = 1; i < w; i++) {
                this.setChar(x + i, y + h, new Texel(new Fill(4, Orientations.VERTICAL)), false);
            }

            return this;
    }

    public Surface drawLineH(int x, int y, int d) {
        for (int i = 0; i <= d; i++) {
            this.setChar(x + i, y, new Texel(new Frame(new int[]{0, 0, this.stroke, this.stroke})), false);
        }

        return this;
    }


    public Surface drawLineV(int x, int y, int d) {
        for (int i = 0; i <= d; i++) {
            this.setChar(x, y + i, new Texel(new Frame(new int[]{this.stroke, this.stroke, 0, 0})), false);
        }

        return this;
    }
    public Surface drawText(int x, int y, String text, int[] bounds) {
        int row = 0;
        int col = 0;
        for (int i = 0; i < text.length(); i++) {
            char chunk = text.charAt(i);

            if (chunk == '\n') {
                row += 1;
                col = 0;
            } else {
                if (col >= bounds[0] && bounds[0] > 0) {
                    col = 0;
                    row++;
                }
                if (row >= bounds[1] && bounds[1] > 0) {
                    break;
                }
                this.setChar(x + col, y + row, new Texel(chunk), false);
                col++;
            }
        }

        return this;
    }

    public Surface drawText(int x, int y, String text) {
        return this.drawText(x, y, text, new int[]{0, 0});
    }

    public Surface drawGauge(int x, int y, int length, double value, double min, double max, Orientations orientation) {
        double innerLength = length - 2;
        double ratio = (max - min) / innerLength;
        double amount = (value - min) / ratio;

        int fulls = (int) Math.floor(amount);
        int overflow = Math.abs((int) Math.floor((amount - fulls) * 8));

        if (orientation == Orientations.HORIZONTAL) {
            this.setChar(x, y, new Texel('\u258c'), true);
            this.setChar(x + length - 1, y, new Texel('\u2590'), true);

            for (int i = 0; i < fulls; i++) {
                this.setChar(x + i + 1, y, new Texel(new Fill(8, Orientations.HORIZONTAL)), true);
            }

            this.setChar(x + fulls + 1, y, new Texel(new Fill(overflow, Orientations.HORIZONTAL)), true);
        } else {
            this.setChar(x, y, new Texel('\u2580'), true);
            this.setChar(x, y + length - 1, new Texel('\u2584'), true);

            for (int i = 0; i < fulls; i++) {
                this.setChar(x, y + length - (i + 2), new Texel(new Fill(8, Orientations.VERTICAL)), true);
            }

            this.setChar(x, y + length - (fulls + 2), new Texel(new Fill(overflow, Orientations.VERTICAL)), true);
        }

        return this;
    }
// \u2612 checked \u2610 unchecked
    // Maybe try ○◉ ▣□ ◇◈

    public Surface drawCheckbox(int x, int y, String label, boolean value) {
        this.setChar(x, y, new Texel(value ? '▣' : '□'), true);
        this.drawText(x + 2, y, label);

        return this;
    }

    //=====-------------------------=====//
    //=====----- Nesting stuff -----=====//
    //=====-------------------------=====//
    public Surface insert(Surface context, int x, int y, boolean showBoxShadow, boolean showBorder,
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
                    this.setChar(x + cx + paddingLeft, y + cy + paddingTop, buff[cy][cx], false);
                }
            }
        }
        if (showBorder) {
            this.drawRect(x, y, context.w + paddingTop + paddingBottom, context.h + paddingLeft + paddingRight);
        }

        if (showName && context.name != null && context.w - 3 > 0) {
            if (showBorder) {
                this.drawText(x + 2, y, context.name, new int[]{context.w - 3, 1});
            } else {
                this.drawText(x, y, context.name, new int[]{context.w - 1, 1});
            }
        }

        if (showBoxShadow) {
            this.drawBoxShadow(x, y, context.w + paddingTop + paddingBottom, context.h + paddingLeft + paddingRight);
        }

        return this;
    }
}
