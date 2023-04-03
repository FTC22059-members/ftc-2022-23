package com.overclockedftc.typographer;

/**
 * <b>Spaced Char</b> is a wrapper class for <i>chars</i>. It adds appropriate invisible characters to allow all characters to appear a consistent width in the telemetry box. Basically, if the character is supported by noto sans mono, it is slightly to thin, so it gets some space. The lookup table is generated with fontRange.py.
 */
public class SpacedChar {
    private char contents;
    private String endSpace;
    private String startSpace;

    private int[][] fontCoverage = new int[][]{new int[]{0, 0}, new int[]{13, 13}, new int[]{32, 126}, new int[]{160, 887}, new int[]{890, 895}, new int[]{900, 906}, new int[]{908, 908}, new int[]{910, 929}, new int[]{931, 993}, new int[]{1008, 1327}, new int[]{6832, 6848}, new int[]{7296, 7304}, new int[]{7424, 7673}, new int[]{7675, 7957}, new int[]{7960, 7965}, new int[]{7968, 8005}, new int[]{8008, 8013}, new int[]{8016, 8023}, new int[]{8025, 8025}, new int[]{8027, 8027}, new int[]{8029, 8029}, new int[]{8031, 8061}, new int[]{8064, 8116}, new int[]{8118, 8132}, new int[]{8134, 8147}, new int[]{8150, 8155}, new int[]{8157, 8175}, new int[]{8178, 8180}, new int[]{8182, 8190}, new int[]{8192, 8292}, new int[]{8294, 8305}, new int[]{8308, 8334}, new int[]{8336, 8348}, new int[]{8352, 8383}, new int[]{8432, 8432}, new int[]{8448, 8543}, new int[]{8580, 8580}, new int[]{8585, 8585}, new int[]{8592, 8597}, new int[]{8604, 8606}, new int[]{8608, 8608}, new int[]{8610, 8612}, new int[]{8614, 8614}, new int[]{8656, 8660}, new int[]{8666, 8667}, new int[]{8678, 8678}, new int[]{8680, 8680}, new int[]{8704, 8718}, new int[]{8720, 8720}, new int[]{8722, 8722}, new int[]{8728, 8730}, new int[]{8734, 8734}, new int[]{8736, 8736}, new int[]{8739, 8739}, new int[]{8743, 8746}, new int[]{8756, 8760}, new int[]{8764, 8765}, new int[]{8769, 8769}, new int[]{8771, 8771}, new int[]{8773, 8773}, new int[]{8775, 8780}, new int[]{8788, 8789}, new int[]{8791, 8791}, new int[]{8799, 8802}, new int[]{8804, 8805}, new int[]{8812, 8812}, new int[]{8814, 8821}, new int[]{8826, 8827}, new int[]{8834, 8841}, new int[]{8846, 8846}, new int[]{8849, 8860}, new int[]{8866, 8869}, new int[]{8884, 8885}, new int[]{8888, 8888}, new int[]{8898, 8900}, new int[]{8902, 8902}, new int[]{8904, 8906}, new int[]{8909, 8910}, new int[]{8912, 8913}, new int[]{8930, 8931}, new int[]{8968, 8971}, new int[]{8976, 8976}, new int[]{8985, 8985}, new int[]{8992, 8993}, new int[]{9014, 9082}, new int[]{9109, 9109}, new int[]{9115, 9134}, new int[]{9136, 9149}, new int[]{9180, 9185}, new int[]{9332, 9333}, new int[]{9472, 9727}, new int[]{9837, 9839}, new int[]{10038, 10038}, new int[]{10072, 10074}, new int[]{10197, 10199}, new int[]{10204, 10204}, new int[]{10214, 10219}, new int[]{10229, 10230}, new int[]{10631, 10632}, new int[]{10659, 10659}, new int[]{10680, 10680}, new int[]{10752, 10752}, new int[]{10757, 10758}, new int[]{11360, 11391}, new int[]{11744, 11858}, new int[]{42560, 42655}, new int[]{42752, 42943}, new int[]{42946, 42954}, new int[]{42997, 43007}, new int[]{43310, 43310}, new int[]{43824, 43883}, new int[]{65024, 65024}, new int[]{65056, 65071}, new int[]{65279, 65279}, new int[]{65371, 65371}, new int[]{65373, 65373}};


    /**
     * Initializes the <b>SpacedChar</b> it checks the <i>content</i> against a big lookup table and adds spaces as needed.
     * @param contents the character to add space around
     */
    public SpacedChar(char contents) {
        this.contents = contents;
        int codePoint = (int) this.contents;
        boolean isSupported = false;
        for (int i = 0; i < fontCoverage.length; i++) {
            if (fontCoverage[i][0] <= codePoint && codePoint <= fontCoverage[i][1]) {
                isSupported = true;
            }
        }

        if (isSupported) {
            endSpace = " ";
            startSpace = " ";
        }
    }

    /**
     * It concatenates the spaces and the content.
     * @return The <i>content</i> with all the proper spacing
     */
    public String render() {
        return this.startSpace + this.contents + this.endSpace;
    }
}
