package org.firstinspires.ftc.teamcode.library.fancyTelemetry;

/**
 * The <b>Arrow Command</b> allows you to reference 8 different angles of arrow. in the 21xx range.
 */
public class Arrow implements Command {

    public Angles angle;

    public Arrow(Angles angle) {
        this.angle = angle;
    }

    public Texel underlay(Texel lower, boolean sameLayer) {
        return new Texel(this);
    }

    public char render() {
        switch (this.angle) {
            case NORTH:
                return '\u2191';
            case EAST:
                return '\u2192';
            case SOUTH:
                return '\u2193';
            case WEST:
                return '\u2190';
            case NORTHEAST:
                return '\u2197';
            case NORTHWEST:
                return '\u2196';
            case SOUTHEAST:
                return '\u2198';
            case SOUTHWEST:
                return '\u2199';
            default:
                return '*';
        }
    }
}