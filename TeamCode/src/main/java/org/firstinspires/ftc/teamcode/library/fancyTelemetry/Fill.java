package org.firstinspires.ftc.teamcode.library.fancyTelemetry;

class Fill implements Command {
    public int amount;
    public Orientations orientation;

    private char[] VERT_BLOCKS = {
            ' ',
            '\u2581',    //     ▁ 	1/8
            '\u2582',    // 	▂ 	2/8
            '\u2583',    // 	▃ 	3/8
            '\u2584',    // 	▄ 	4/8
            '\u2585',    // 	▅ 	5/8
            '\u2586',    // 	▆ 	6/8
            '\u2587',    // 	▇ 	7/8
            '\u2588'     //     █ 	8/8
    };

    private char[] HORI_BLOCKS = {
            ' ',
            '\u258F',    //     ▏   1/8
            '\u258E',    // 	▎ 	2/8
            '\u258D',    // 	▍ 	3/8
            '\u258C',    // 	▌ 	4/8
            '\u258B',    // 	▋ 	5/8
            '\u258A',    // 	▊ 	6/8
            '\u2589',    // 	▉ 	7/8
            '\u2588'     //     █ 	8/8
    };

    public Fill(int amount, Orientations orientation) {
        this.amount = amount;
        this.orientation = orientation;
    }

    public Texel underlay(Texel layered, boolean sameLayer) {
        return new Texel(this);
    }

    public char render() {
        if (this.orientation == Orientations.VERTICAL) {
            return this.VERT_BLOCKS[this.amount];
        } else {
            return this.HORI_BLOCKS[this.amount];
        }
    }
}