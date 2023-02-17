package org.firstinspires.ftc.teamcode.library.fancyTelemetry;

import java.util.Arrays;
import java.util.List;

/**
 * A <b>Texel</b> ("text" + "pixel") represents a character or a command. It is type stored in the buffers.
 */
public class Texel {
    public String content;
    public Command command;
    public Boolean isCommand;

    /**
     * Initializes the Texel as an empty space.
     */
    public Texel() {
        this.content = "";
        this.isCommand = false;
    }

    /**
     * Initializes the Texel with content.
     *
     * @param content The content to initialize it with. Should be one character.
     */
    public Texel(String content) {
        this.content = content;
        this.isCommand = false;
    }

    /**
     * Initializes the Texel as a command.
     *
     * @param command The command to initialize it with.
     */
    public Texel(Command command) {
        this.command = command;
        this.isCommand = true;

    }

    /**
     * Layers the Texel on top of another Texel
     *
     * @param layered   The Texel that is under it
     * @param sameLayer Whether the Texels are on the same layer (generally determines if they merge or not).
     * @return The resulting Texel
     */
    public Texel underlay(Texel layered, Boolean sameLayer) {
        if (this.isCommand) {
            return this.command.underlay(layered, sameLayer);
        } else {
            return this;
        }
    }
}
