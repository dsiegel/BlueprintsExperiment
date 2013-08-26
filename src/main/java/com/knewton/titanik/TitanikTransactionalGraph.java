package com.knewton.titanik;

import com.google.common.collect.AbstractIterator;
import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.util.ExceptionFactory;
import com.tinkerpop.blueprints.util.StringFactory;

import java.util.Iterator;
import java.util.UUID;

// I Made a mistake.  Anything I return needs to be wrapped in a NewTitanikVertex, because the user might modify it.
// !!!!!

public class TitanikTransactionalGraph implements TransactionalGraph {
    private static final Features FEATURES = new Features();

    static {
        FEATURES.supportsDuplicateEdges = false;
        FEATURES.supportsSelfLoops = true;
        FEATURES.supportsSerializableObjectProperty = true;
        FEATURES.supportsBooleanProperty = true;
        FEATURES.supportsDoubleProperty = true;
        FEATURES.supportsFloatProperty = true;
        FEATURES.supportsIntegerProperty = true;
        FEATURES.supportsPrimitiveArrayProperty = true;
        FEATURES.supportsUniformListProperty = true;
        FEATURES.supportsMixedListProperty = true;
        FEATURES.supportsLongProperty = true;
        FEATURES.supportsMapProperty = true;
        FEATURES.supportsStringProperty = true;

        FEATURES.ignoresSuppliedIds = true;
        FEATURES.isPersistent = false;
        FEATURES.isWrapper = false;

        FEATURES.supportsIndices = false;
        FEATURES.supportsKeyIndices = false;
        FEATURES.supportsVertexKeyIndex = false;
        FEATURES.supportsEdgeKeyIndex = false;
        FEATURES.supportsVertexIndex = false;
        FEATURES.supportsEdgeIndex = false;
        FEATURES.supportsTransactions = true;
        FEATURES.supportsVertexIteration = false;
        FEATURES.supportsEdgeIteration = false;
        FEATURES.supportsEdgeRetrieval = false;
        FEATURES.supportsVertexProperties = true;
        FEATURES.supportsEdgeProperties = true;
        FEATURES.supportsThreadedTransactions = false;
    }


    private final Graph readGraph;
    private final TitanikEventConsumer consumer;

    private TitanikTransactionEvent tx;

    public TitanikTransactionalGraph(Graph readGraph, TitanikEventConsumer consumer) {
        this.readGraph = readGraph;
        this.consumer = consumer;
        tx = new TitanikTransactionEvent();
    }

    @Override
    public void shutdown() {
        tx = null;
    }

    @Override
    public void commit() {
        consumer.consume(tx);
        tx = new TitanikTransactionEvent();
    }

    @Override
    public void rollback() {
        tx = new TitanikTransactionEvent();
    }

    @Override
    public Vertex addVertex(Object id) {
        if (id == null ) {
            id = UUID.randomUUID().toString(); //TODO(dsiegel): how do we handle our ids?
        }
        Vertex vertex = getVertex(id);
        if (vertex == null) {
            NewTitanikVertex newTitanikVertex = new NewTitanikVertex(id, this);
            tx.newVertices.put(id, newTitanikVertex);
            return newTitanikVertex;
        } else {
            return vertex;
        }
    }

    @Override
    public Vertex getVertex(Object id) {
        TitanikVertex vertex = tx.newVertices.get(id);
        if (vertex == null) {
            Vertex persistedVertex = readGraph.getVertex(id);
            if (persistedVertex == null) {
                return null;
            } else {
                PersistedTitanikVertex persistedTitanikVertex = new PersistedTitanikVertex(this, persistedVertex);
                tx.newVertices.put(id, persistedTitanikVertex);
                return persistedTitanikVertex;
            }
        } else {
            return vertex;
        }
    }

    @Override
    public Iterable<Vertex> getVertices() {
        return new Iterable<Vertex>() {
            public Iterator<Vertex> iterator() {
                return new AbstractIterator<Vertex>() {
                    Iterator<Vertex> persistedVertexIterator = readGraph.getVertices().iterator();
                    Iterator<TitanikVertex> txIterator = tx.newVertices.values().iterator();

                    @Override
                    protected Vertex computeNext() {
                        while (persistedVertexIterator.hasNext()) {
                            Vertex next = persistedVertexIterator.next();
                            if (!tx.newVertices.containsKey(next.getId())) {
                                return next;
                            }
                        }
                        while (txIterator.hasNext()) {
                            return txIterator.next();
                        }
                        return endOfData();
                    }
                };
            }
        };
    }

    @Override
    public Iterable<Vertex> getVertices(final String key, final Object value) {
        return new Iterable<Vertex>() {
            Iterator<Vertex> persistedVertexIterator = readGraph.getVertices(key, value).iterator();
            Iterator<TitanikVertex> txIterator = tx.newVertices.values().iterator();

            public Iterator<Vertex> iterator() {
                return new AbstractIterator<Vertex>() {
                    @Override
                    protected Vertex computeNext() {
                        while (persistedVertexIterator.hasNext()) {
                            Vertex next = persistedVertexIterator.next();
                            if (!tx.newVertices.containsKey(next.getId())) {
                                return next;
                            }
                        }
                        while (txIterator.hasNext()) {
                            TitanikVertex next = txIterator.next();
                            if (next.getProperty(key).equals(value)) {
                                return next;
                            }
                        }
                        return endOfData();
                    }
                };
            }
        };
    }

    @Override
    public Features getFeatures() {
        return FEATURES;
    }

    @Override
    public void removeVertex(Vertex vertex) {
        for (Edge edge : vertex.getEdges(Direction.BOTH)) {
            this.removeEdge(edge);
        }

        tx.deletedVertices.add(vertex.getId()); //TODO(dsiegel): should this remove be conditional on the vertex being in the persisted set?
        tx.newVertices.remove(vertex.getId());
    }

    @Override
    public Edge addEdge(Object id, Vertex outVertex, Vertex inVertex, String label) {
        if (label == null)
            throw ExceptionFactory.edgeLabelCanNotBeNull();

        TitanikVertex titanikOutVertex = (TitanikVertex) outVertex;
        TitanikVertex titanikInVertex = (TitanikVertex) inVertex;

        TitanikEdge outEdge = titanikOutVertex.getOutEdge(label, titanikInVertex.getId());
        if (outEdge == null) {
            NewTitanikEdge edge = new NewTitanikEdge(this, titanikOutVertex, titanikInVertex, label);
            titanikOutVertex.addOutEdge(edge);
            titanikInVertex.addInEdge(edge);
            return edge;
        } else {
            return outEdge;
        }
    }

    @Override
    public Edge getEdge(Object id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeEdge(Edge edge) {
        TitanikVertex inVertex = (TitanikVertex) edge.getVertex(Direction.IN);
        TitanikVertex outVertex = (TitanikVertex) edge.getVertex(Direction.OUT);
        inVertex.removeInEdge(edge);
        outVertex.removeOutEdge(edge);
    }

    @Override
    public Iterable<Edge> getEdges() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterable<Edge> getEdges(String key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public GraphQuery query() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public void stopTransaction(Conclusion conclusion) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        // TODO(dsiegel): better graph tostring
        return StringFactory.graphString(this, "new vertices: " + tx.newVertices.size());
    }
}
