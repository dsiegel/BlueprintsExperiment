package com.knewton.titanik;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.VertexQuery;

public class PersistedTitanikVertex extends AbstractPersistedTitanikElement implements TitanikVertex {

    public PersistedTitanikVertex(TitanikTransactionalGraph graph, Vertex persistedElement) {
        super(graph, persistedElement);
    }

    @Override
    public Iterable<Edge> getEdges(Direction direction, String... labels) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterable<Vertex> getVertices(Direction direction, String... labels) {
        throw new UnsupportedOperationException();
    }

    @Override
    public VertexQuery query() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Edge addEdge(String label, Vertex inVertex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TitanikEdge getOutEdge(String label, Object inVertexId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TitanikEdge getInEdge(String label, Object outVertexId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addOutEdge(TitanikEdge edge) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addInEdge(TitanikEdge edge) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeInEdge(Edge edge) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeOutEdge(Edge edge) {
        throw new UnsupportedOperationException();
    }
}
