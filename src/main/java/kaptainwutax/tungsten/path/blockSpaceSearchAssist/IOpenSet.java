package kaptainwutax.tungsten.path.blockSpaceSearchAssist;

import kaptainwutax.tungsten.path.blockSpaceSearchAssist.BlockNode;

public interface IOpenSet {

    void insert(BlockNode node);

    boolean isEmpty();

    BlockNode removeLowest();

    void update(BlockNode node);
}