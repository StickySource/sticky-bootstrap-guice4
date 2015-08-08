package net.stickycode.bootstrap.tck.component;

import net.stickycode.stereotype.StickyComponent;

@StickyComponent
public class AtTheBottom
    extends AbstractInTheMiddle {

}

interface UpTheTop {

}

abstract class AbstractInTheMiddle
    implements UpTheTop {

}
