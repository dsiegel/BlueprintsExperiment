package com.knewton.titanik;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.ExceptionFactory;

import java.util.Set;

/**
 */
public class NewTitanikEdge extends AbstractNewTitanikElement implements TitanikEdge {
    private final TitanikVertex outVertex;
    private final TitanikVertex inVertex;
    private final String label;

    public NewTitanikEdge(TitanikTransactionalGraph graph, TitanikVertex outVertex, TitanikVertex inVertex, String label) {
        super(null, graph);
        this.outVertex = outVertex;
        this.inVertex = inVertex;
        this.label = label;
    }

    @Override
    public Vertex getVertex(Direction direction) throws IllegalArgumentException {
        if (direction.equals(Direction.IN))
            return this.inVertex;
        else if (direction.equals(Direction.OUT))
            return this.outVertex;
        else
            throw ExceptionFactory.bothIsNotSupported();
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void remove() {
        getGraph().removeEdge(this);
    }

    @Override
    public Object getId() {
        // TODO(dsiegel): what do I do, edges don't have ids
        return null;
    }
}
