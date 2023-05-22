package com.overclockedftc.typographer;

/**
 * The <b>Transparent Command</b> represents a hole in a layer. It always shows the character that is layered below it.
 */
public class Transparent implements Command {
    @Override
    public Texel underlay(Texel lower, boolean sameLayer) {
        return lower;
    }

    @Override
    public char render() {
        return ' ';
    }
}
