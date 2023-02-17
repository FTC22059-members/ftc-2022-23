package org.firstinspires.ftc.teamcode.library.fancyTelemetry;

import java.util.HashMap;

/**
 * A <b>Graphics Context</b> represents a display. There is one root <b>Graphics
 * Context</b>, and it can have several sub <b>Graphics Contexts</b> to
 * represent other windows.
 */
public class GraphicsContext {
    private final int x;
    private final int y;
    private final int w;
    private final int h;
    private final GraphicsContext parent;
    public boolean hasParent;
    public boolean framed;
    public boolean boxShadow;
    private Texel[][] buffer;
    private int stroke;
    private final String id;
    private HashMap<String, GraphicsContext> children = new HashMap<>();
    private Boolean showId;

    /**
     * Initializes a <b>Graphics Context</b> that is a child of another <b>Graphics
     * Context</b>
     *
     * @param x      The x position of the <b>Graphics Context</b>
     * @param y      The y position of the <b>Graphics Context</b>
     * @param w      The width of the <b>Graphics Context</b>
     * @param h      The height of the <b>Graphics Context</b>
     * @param parent The parent <b>Graphics Context</b>
     * @param id     The id (nad also name) of the <b>Graphics Context</b>
     */
    public GraphicsContext(int x, int y, int w, int h, GraphicsContext parent, String id) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.hasParent = parent != null;
        this.parent = parent;

        this.framed = this.hasParent;
        this.boxShadow = false;
        this.buffer = new Texel[h][w];

        this.stroke = 1;
        this.id = id;
        this.showId = this.hasParent;
    }

    /**
     * Initializes the root graphics context. It has no <i>id</i>, <i>x</i>,
     * <i>y</i>, or <i>parent</i>
     *
     * @param w The width of the <b>Graphics Context</b>
     * @param h The height of the <b>Graphics Context</b>
     */
    public GraphicsContext(int w, int h) {
        this.x = 0;
        this.y = 0;
        this.w = w;
        this.h = h;
        this.hasParent = false;
        this.parent = null;

        this.framed = false;
        this.boxShadow = false;
        this.buffer = new Texel[h][w];

        this.stroke = 1;
        this.id = "";
        this.showId = false;
    }

    // Buffer Actions

    /**
     * Sets the value of a particular Texel
     *
     * @param x             The x coordinate of the Texel to set.
     * @param y             The x coordinate of the Texel to set.
     * @param val           The value to set it to.
     * @param sameLayer     Whether the Texels should be treated as though they were
     *                      on the same layer (allows merging or not).
     * @param ignorePadding Whether to allow Texels to be set in the areas where a
     *                      frame would be (if applicable).
     */
    public void setChar(int x, int y, Texel val, boolean sameLayer, boolean ignorePadding) {
        int padding = 0;
        if (!ignorePadding && this.framed) {
            padding = 1;
        }

        x += padding;
        y += padding;
        if (0 <= x && x < this.w && 0 <= y && y < this.h) {
            // this.buffer[y][x] = this.overlay(this.buffer[y][x], val, sameLayer);
            this.buffer[y][x] = val.underlay(this.buffer[y][x], sameLayer);
        }
    }

    /**
     * Clears only this <i>buffer</i>.
     */
    public void clearSelf() {
        this.buffer = new Texel[h][w];
    }

    /**
     * Clears its own <i>buffer</i> and the <i>buffers</i> of all of its children.
     */
    public void clearAll() {
        for (HashMap.Entry<String, GraphicsContext> child : this.children.entrySet()) {
            child.getValue().clearSelf();
        }
        this.clearSelf();
    }

    /**
     * Calls <i>render()</i> on all of it's children, flattening them with
     * <i>overlay()</i>, then returns the result. If it is a root <b>Graphics
     * Context</b>, it renders all of the commands and returns the result to get
     * sent to the <code>Driver Station</code>.
     * @return A Buffer Array with all of it's children rendered in it.
     */
    public Texel[][] render() {
        if (this.children != null) {
            for (HashMap.Entry<String, GraphicsContext> mapEntry : this.children.entrySet()) {
                GraphicsContext child = mapEntry.getValue();
                Texel[][] buff = child.render();

                for (int y = 0; y < child.h; y++) {
                    for (int x = 0; x < child.w; x++) {
                        if (buff[y][x] != null) {
                            this.setChar(x + child.x, y + child.y, buff[y][x], false, false);
                        }
                    }
                }
                if (child.boxShadow)
                    this.drawBoxShadow(child.x, child.y, child.w, child.h);
            }
        }

        if (this.framed) {
            int w = this.w;
            int h = this.h;
            w -= 1;
            h -= 1;

            // this.setChar(0, 0, `%f !sides 0${this.#stroke}0${this.#stroke} !merge
            // false`,{"ignore-padding":true});
            this.setChar(0, 0, new Texel(new Frame(new int[]{0, this.stroke, 0, this.stroke}, false)), false, true);
            this.setChar(w, 0, new Texel(new Frame(new int[]{0, this.stroke, this.stroke, 0}, false)), false,
                    true);
            this.setChar(0, h, new Texel(new Frame(new int[]{this.stroke, 0, 0, this.stroke}, false)), false,
                    true);
            this.setChar(w, h, new Texel(new Frame(new int[]{this.stroke, 0, this.stroke, 0}, false)), false,
                    true);

            for (int i = 1; i < h; i++) {
                this.setChar(0, i, new Texel(new Frame(new int[]{this.stroke, this.stroke, 0, 0}, false)), false,
                        true);
                this.setChar(w, i, new Texel(new Frame(new int[]{this.stroke, this.stroke, 0, 0}, false)),
                        false, true);
            }

            for (int i = 1; i < w; i++) {
                this.setChar(i, 0, new Texel(new Frame(new int[]{0, 0, this.stroke, this.stroke}, false)), false,
                        true);
                this.setChar(i, h, new Texel(new Frame(new int[]{0, 0, this.stroke, this.stroke}, false)),
                        false, true);
            }

            if (this.showId) {

                if (w - 3 > 0) {
                    this.drawText(2, 0, this.id, true, new int[]{w - 3, 1});
                }
            }
        }

        return this.buffer;
    }

    // Setters

    /**
     * Sets whether or not to display a frame around the inside of the <b>Graphics
     * Context</b>
     * @param framed The value to set
     * @return The <b>Graphics Context</b>
     */
    public GraphicsContext setFramed(boolean framed) {
        this.framed = framed;

        return this;
    }

    /**
     * Sets whether or not to display a box shadow around the outside bottom right
     * of the <b>Graphics Context</b>
     * @param boxShadow The value to set
     * @return The <b>Graphics Context</b>
     */
    public GraphicsContext setBoxShadow(boolean boxShadow) {
        this.boxShadow = boxShadow;

        return this;
    }

    /**
     * Sets whether or not to display the id of the <b>Graphics Context</b> on the
     * top edge
     * @param showId The value to set
     * @return The <b>Graphics Context</b>
     */
    public GraphicsContext setShowId(boolean showId) {
        this.showId = showId;

        return this;
    }

    // Drawers

    /**
     * Draw a rectangle on the <b>Buffer</b>
     * @param x The x coordinate of the top left corner
     * @param y The y coordinate of the top left corner
     * @param w The width of the rectangle
     * @param h The height of the rectangle
     * @return The <b>Graphics Context</b>
     */
    public GraphicsContext drawRect(int x, int y, int w, int h) {
        w -= 1;
        h -= 1;

        this.setChar(x, y, new Texel(new Frame(new int[]{0, this.stroke, 0, this.stroke})), true, false);
        this.setChar(x + w, y, new Texel(new Frame(new int[]{0, this.stroke, this.stroke, 0})), true, false);
        this.setChar(x, y + h, new Texel(new Frame(new int[]{this.stroke, 0, 0, this.stroke})), true, false);
        this.setChar(x + w, y + h, new Texel(new Frame(new int[]{this.stroke, 0, this.stroke, 0})), true, false);

        for (int i = 1; i < h; i++) {
            this.setChar(x, y + i, new Texel(new Frame(new int[]{this.stroke, this.stroke, 0, 0})), true, false);
            this.setChar(x + w, y + i, new Texel(new Frame(new int[]{this.stroke, this.stroke, 0, 0})), true, false);
        }

        for (int i = 1; i < w; i++) {
            this.setChar(x + i, y, new Texel(new Frame(new int[]{0, 0, this.stroke, this.stroke})), true, false);
            this.setChar(x + i, y + h, new Texel(new Frame(new int[]{0, 0, this.stroke, this.stroke})), true, false);
        }

        return this;
    }

    /**
     * Draw a box shadow on the <b>Buffer</b>
     * @param x The x coordinate of the top left corner
     * @param y The y coordinate of the top left corner
     * @param w The width of the box shadow
     * @param h The height of the box shadow
     * @return The <b>Graphics Context</b>
     */
    public GraphicsContext drawBoxShadow(int x, int y, int w, int h) {
        this.setChar(x + w, y, new Texel("\u258C"), false, false);
        this.setChar(x, y + h, new Texel("\u2580"), false, false);
        this.setChar(x + w, y + h, new Texel("\u2598"), false, false);

        for (int i = 1; i < h; i++) {
            this.setChar(x + w, y + i, new Texel("\u258C"), false, false);
        }

        for (int i = 1; i < w; i++) {
            this.setChar(x + i, y + h, new Texel("\u2580"), false, false);
        }

        return this;
    }

    /**
     * Draw a horizontal line on the <b>Buffer</b>
     * @param x The x coordinate of the leftmost point on the line
     * @param y The y coordinate of the line
     * @param d The length of the line
     * @return The <b>Graphics Context</b>
     */
    public GraphicsContext drawLineH(int x, int y, int d) {
        for (int i = 0; i <= Math.abs(d); i++) {
            this.setChar(x + i, y, new Texel(new Frame(new int[]{0, 0, this.stroke, this.stroke})), false, false);
        }

        return this;
    }

    /**
     * Draw a vertical line on the <b>Buffer</b>
     * @param x The x coordinate of the line
     * @param y The y coordinate of the top point on the line
     * @param d The length of the line
     * @return The <b>Graphics Context</b>
     */
    public GraphicsContext drawLineV(int x, int y, int d) {
        for (int i = 0; i <= Math.abs(d); i++) {
            this.setChar(x, y + i, new Texel(new Frame(new int[]{this.stroke, this.stroke, 0, 0})), false, false);
        }

        return this;
    }

    /**
     * Draw text on the <b>Buffer</b>.
     *
     * @param x             The x coordinate of the top leftmost character in the text
     * @param y             The y coordinate of the top leftmost character in the text
     * @param text          The text to display
     * @param ignorePadding Whether to draw the text over any padding (the frame)
     * @param bounds        A 2 item array with the number of columns [0] and the number of rows [1]
     * @return The <b>Graphics Context</b>
     */
    public GraphicsContext drawText(int x, int y, String text, boolean ignorePadding, int[] bounds) {
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
                this.setChar(x + col, y + row, new Texel(Character.toString(chunk)), false, ignorePadding);
                col++;
            }
        }

        return this;
    }

    public GraphicsContext drawText(int x, int y, String text, boolean ignorePadding) {
        return this.drawText(x, y, text, ignorePadding, new int[]{0, 0});
    }

    /**
     * Draw a gauge on the <b>Buffer</b>.
     *
     * @param x           The x coordinate of the left of the gauge
     * @param y           The y coordinate of the gauge
     * @param length      The length of the gauge
     * @param value       The value displayed on the gauge
     * @param min         The minimum value of the gauge
     * @param max         The maximum value of the gauge
     * @param orientation The orientation of the gauge
     * @return The <b>Graphics Context</b>
     */

    public GraphicsContext drawGauge(int x, int y, int length, double value, double min, double max, Orientations orientation) {
        double innerLength = length - 2;
        double ratio = (max - min) / innerLength;
        double amount = (value - min) / ratio;

        int fulls = (int) Math.floor(amount);
        int overflow = (int) Math.floor((amount - fulls) * 8);

        if (orientation == Orientations.HORIZONTAL) {
            this.setChar(x, y, new Texel("\u258c"), true, false);
            this.setChar(x + length - 1, y, new Texel("\u2590"), true, false);

            for (int i = 0; i < fulls; i++) {
                this.setChar(x + i + 1, y, new Texel(new Fill(8, Orientations.HORIZONTAL)), true, false);
            }

            this.setChar(x + fulls + 1, y, new Texel(new Fill(overflow, Orientations.HORIZONTAL)), true, false);
        } else {
            this.setChar(x, y, new Texel("\u2580"), true, false);
            this.setChar(x, y + length - 1, new Texel("\u2584"), true, false);

            for (int i = 0; i < fulls; i++) {
                this.setChar(x, y + length - (i + 2), new Texel(new Fill(8, Orientations.VERTICAL)), true, false);
            }

            this.setChar(x, y + length - (fulls + 2), new Texel(new Fill(overflow, Orientations.VERTICAL)), true, false);
        }

        return this;
    }

    // \u2612 checked \u2610 unchecked
    // Maybe try ○◉ ▣□ ◇◈

    /**
     * Draw a checkbox on the <b>Buffer</b>
     *
     * @param x     The x coordinate of the checkbox
     * @param y     They y coordinate of the checkbox
     * @param label The label for the checkbox
     * @param value The value displayed in the checkbox
     * @return The <b>Graphics Context</b>
     */
    public GraphicsContext drawCheckbox(int x, int y, String label, boolean value) {
        this.setChar(x, y, new Texel(value ? "\u2612" : "\u2610"), true, false);
        this.drawText(x + 2, y, label, false);

        return this;
    }

    // Nesting Stuff

    /**
     * Add a child <b>Graphics Context</b>
     *
     * @param x  The x coordinate of the top left corner of the child <b>Graphics Context</b>
     * @param y  The x coordinate of the top left corner of the child <b>Graphics Context</b>
     * @param w  The width of the child <b>Graphics Context</b>
     * @param h  The height of the child <b>Graphics Context</b>
     * @param id The id of the child <b>Graphics Context</b>
     * @return The <b>Graphics Context</b>
     */
    public GraphicsContext addContext(int x, int y, int w, int h, String id) {
        GraphicsContext ngc = new GraphicsContext(x, y, w, h, this, id);
        this.children.put(id, ngc);

        return ngc;
    }

    /**
     * Removes a child <b>Graphics Context</b>
     *
     * @param context The child <b>Graphics Context</b> to remove
     * @return The <b>Graphics Context</b>
     */
    public GraphicsContext removeContext(GraphicsContext context) {
        this.children.remove(context.id);

        return this;
    }

    /**
     * Removes a child <b>Graphics Context</b>
     *
     * @param id The ID/name of the child <b>Graphics Context</b> to remove
     * @return The <b>Graphics Context</b>
     */
    public GraphicsContext removeContext(String id) {
        this.children.remove(id);

        return this;
    }

    /**
     * Get the parent <b>Graphics Context</b> if it exists
     *
     * @return The <b>Graphics Context</b>
     */
    public GraphicsContext getParent() {
        return this.parent;
    }
}
