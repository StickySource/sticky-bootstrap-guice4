package net.stickycode.bootstrap.tck.component;

import net.stickycode.stereotype.StickyComponent;

@StickyComponent
public class TheSubInterface
    implements SubInterface {

}

interface SuperInterface {

}

interface SubInterface
    extends SuperInterface {

}
