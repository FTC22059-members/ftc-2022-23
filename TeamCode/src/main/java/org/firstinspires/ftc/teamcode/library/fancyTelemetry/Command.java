package org.firstinspires.ftc.teamcode.library.fancyTelemetry;

public interface Command {
    /**
     * @param lower     The Texel that is getting layered underneath this one.
     * @param sameLayer Whether the Texels are an the same layer (generally
     *                  determines if they are allowed to merge).
     * @return The Texel that results from the overlay.
     */
    Texel underlay(Texel lower, boolean sameLayer);

    /**
     * Resolves the command and all of its flags into a single character.
     *
     * @return Returns the result of the execution.
     */
    char render();
}
