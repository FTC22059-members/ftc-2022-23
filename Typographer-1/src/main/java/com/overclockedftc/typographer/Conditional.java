package com.overclockedftc.typographer;

public class Conditional implements Command {

    public Boolean condition;
    public Texel ifTrue;
    public Texel ifFalse;

    public Conditional(Boolean condition, Texel ifTrue) {
        this.condition = condition;
        this.ifTrue = ifTrue;
        this.ifFalse = new Texel();
    }

    public Conditional(Boolean condition, Texel ifTrue, Texel ifFalse) {
        this.condition = condition;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
    }

    public Texel underlay(Texel lower, boolean sameLayer) {
        return new Texel(this);
    }

    public char render() {
        if (this.condition) {
            return this.ifTrue.render();
        } else {
            return this.ifFalse.render();
        }
    }
}
