package org.firstinspires.ftc.teamcode.library.fancyTelemetry;

/**
 * The <b>Frame Command</b> allows you to easily reference the 25xx unicode block for box-drawing characters.
 */
public class Frame implements Command {
    public int[] sides;
    public boolean merge;

    // This is a 4-dimensional array of box-drawing chars.
    // FRAME_CHARS[1][2][0][1] is a char with A thin top, a thick bottom, no left, and a thin right
    // Iff it has all 0s, it returns an asterisk.

    private final String[][][][] FRAME_CHARS = {
            {//no top
                    { //no bottom
                            {"*", "\u2576", "\u257a"},//no left
                            {"\u2574", "\u2500", "\u257c"},//thin left
                            {"\u2578", "\u257e", "\u2501"}//thick left
                    },

                    {//thin bottom
                            {"\u2577", "\u250c", "\u250d"},//no left
                            {"\u2510", "\u252c", "\u252d"},//thin left
                            {"\u2511", "\u252e", "\u252f"}//thick left
                    },

                    {//thick bottom
                            {"\u257b", "\u250e", "\u250f"},//no left
                            {"\u2512", "\u2530", "\u2532"},//thin left
                            {"\u2513", "\u2531", "\u2533"}//thick left
                    }
            },
            {//thin top
                    { //no bottom
                            {"\u2575", "\u2514", "\u2515"},//no left
                            {"\u2518", "\u2534", "\u2536"},//thin left
                            {"\u2519", "\u2535", "\u2537"}//thick left
                    },

                    {//thin bottom
                            {"\u2502", "\u251c", "\u251d"},//no left
                            {"\u2524", "\u253c", "\u253e"},//thin left
                            {"\u2525", "\u253d", "\u253f"}//thick left
                    },

                    {//thick bottom
                            {"\u257d", "\u251f", "\u2522"},//no left
                            {"\u2527", "\u2541", "\u2545"},//thin left
                            {"\u252a", "\u2546", "\u2548"}//thick left
                    }
            },
            {//thick top
                    { //no bottom
                            {"\u2579", "\u2516", "\u2517"},//no left
                            {"\u251a", "\u2538", "\u253a"},//thin left
                            {"\u251b", "\u2539", "\u253b"}//thick left
                    },

                    {//thin bottom
                            {"\u257f", "\u251e", "\u2521"},//no left
                            {"\u2526", "\u2540", "\u2544"},//thin left
                            {"\u2529", "\u2543", "\u2547"}//thick left
                    },

                    {//thick bottom
                            {"\u2503", "\u2520", "\u2523"},//no left
                            {"\u2528", "\u2542", "\u254a"},//thin left
                            {"\u252b", "\u2549", "\u254b"}//thick left
                    }
            }
    };

    /**
     * Initializes the <b>Frame Command</b> with <i>merge</i> set to <code>True</code>.
     *
     * @param sides The sides that the frame should connect <code>{top, bottom, left, right}</code>. For no connection, use <code>0</code>. <code>1</code> is a thin line, <code>2</code> is a thick line.
     */
    public Frame(int[] sides) {
        this.sides = sides;
        this.merge = true;
    }

    /**
     * Initializes the <b>Frame Command</b>.
     *
     * @param sides The sides that the frame should connect <code>{top, bottom, left, right}</code>. For no connection, use <code>0</code>. <code>1</code> is a thin line, <code>2</code> is a thick line.
     * @param merge Whether to merge the frame with other frames if they are on the same layer.
     */
    public Frame(int[] sides, boolean merge) {
        this.sides = sides;
        this.merge = merge;
    }

    @Override
    public Texel underlay(Texel lower, boolean sameLayer) {
        if (lower != null && lower.isCommand) {
            if (lower.command instanceof Frame && this.merge && ((Frame) lower.command).merge && sameLayer) {
                return new Texel(new Frame(new int[]{
                        Math.max(this.sides[0], ((Frame) lower.command).sides[0]),
                        Math.max(this.sides[1], ((Frame) lower.command).sides[1]),
                        Math.max(this.sides[2], ((Frame) lower.command).sides[2]),
                        Math.max(this.sides[3], ((Frame) lower.command).sides[3])
                }, this.merge && ((Frame) lower.command).merge));
            } else {
                return new Texel(this);
            }
        } else {
            return new Texel(this);
        }
    }

    @Override
    public char render() {
        return this.FRAME_CHARS[this.sides[0]][this.sides[1]][this.sides[2]][this.sides[3]].charAt(0);
    }
}
