package kaptainwutax.tungsten.path.calculators;

import kaptainwutax.tungsten.path.Node;

public interface IOpenSet {

    void insert(Node node);

    boolean isEmpty();

    Node removeLowest();

    void update(Node node);
}