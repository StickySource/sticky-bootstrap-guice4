package net.stickycode.bootstrap.tck.component;

import net.stickycode.stereotype.StickyComponent;

@StickyComponent
public class Hydra
    implements OneUp {

}

interface OneUp
    extends TwoUp, ThreeUp {

}

interface TwoUp
    extends FourUp {

}

interface ThreeUp
    extends FiveUp {

}

interface FourUp
    extends SixUp {

}

interface FiveUp {

}

interface SixUp {

}
