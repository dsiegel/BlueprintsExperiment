package com.knewton.titanik;

import com.google.common.collect.*;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.VertexQuery;
import com.tinkerpop.blueprints.util.ElementHelper;
import com.tinkerpop.blueprints.util.VerticesFromEdgesIterable;

import java.util.*;

/**
 *
 */
public class NewTitanikVertex extends AbstractNewTitanikElement implements TitanikVertex {

    /**
     * Map Label -> Destination Vertex ID -> Edge
     */
    private final Map<String, Map<Object, Edge>> outEdges;

    /**
     * Map: Label -> Source Vertex ID -> Edge
     */
    private final Map<String, Map<Object, Edge>> inEdges;


    // I don't think we need this....Its a new vertex...so what is there to delete?
    //private final Set<String> deletedProperties;

    public NewTitanikVertex(Object id, TitanikTransactionalGraph graph) {
        super(id, graph);
        outEdges = Maps.newHashMap();
        inEdges = Maps.newHashMap();
    }



    @Override
    public Edge addEdge(String label, Vertex inVertex) {
        return this.getGraph().addEdge(null, this, inVertex, label);
    }

    @Override
    public Iterable<Edge> getEdges(Direction direction, String... labels) {
        switch (direction) {
            case OUT:
                return getOutEdges(labels);
            case IN:
                return getInEdges(labels);
            default:
                return Iterables.concat(getOutEdges(labels), getInEdges(labels));
        }
    }

    private Iterable<Edge> getOutEdges(final String... labels) {
        if (labels.length == 0) {
            final List<Edge> totalEdges = Lists.newArrayList();
            for (Map<Object, Edge> edgeMap : outEdges.values()) {
                totalEdges.addAll(edgeMap.values());
            }
            return totalEdges;
        } else if (labels.length == 1) {
            final Map<Object, Edge> edgeMap = this.outEdges.get(labels[0]);
            if (null == edgeMap) {
                return Collections.emptyList();
            } else {
                return Lists.newArrayList(edgeMap.values());
            }
        } else {
            final List<Edge> totalEdges = new ArrayList<Edge>();
            for (final String label : labels) {
                final Map<Object, Edge> edgeMap = this.outEdges.get(label);
                if (null != edgeMap) {
                    totalEdges.addAll(edgeMap.values());
                }
            }
            return totalEdges;
        }
    }

    private Iterable<Edge> getInEdges(final String... labels) {
        if (labels.length == 0) {
            final List<Edge> totalEdges = Lists.newArrayList();
            for (Map<Object, Edge> edgeMap : inEdges.values()) {
                totalEdges.addAll(edgeMap.values());
            }
            return totalEdges;
        } else if (labels.length == 1) {
            final Map<Object, Edge> edgeMap = this.inEdges.get(labels[0]);
            if (null == edgeMap) {
                return Collections.emptyList();
            } else {
                return Lists.newArrayList(edgeMap.values());
            }
        } else {
            final List<Edge> totalEdges = new ArrayList<Edge>();
            for (final String label : labels) {
                final Map<Object, Edge> edgeMap = this.inEdges.get(label);
                if (null != edgeMap) {
                    totalEdges.addAll(edgeMap.values());
                }
            }
            return totalEdges;
        }
    }

    @Override
    public Iterable<Vertex> getVertices(Direction direction, String... labels) {
        return new VerticesFromEdgesIterable(this, direction, labels);
    }

    @Override
    public VertexQuery query() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove() {
        this.getGraph().removeVertex(this);
    }

    @Override
    public TitanikEdge getOutEdge(String label, Object inVertexId) {
        Map<Object, Edge> edgeMap = this.outEdges.get(label);
        if (edgeMap == null) {
            return null;
        } else {
            return (TitanikEdge) edgeMap.get(inVertexId);
        }
    }

    @Override
    public TitanikEdge getInEdge(String label, Object outVertexId) {
        Map<Object, Edge> edgeMap = this.inEdges.get(label);
        if (edgeMap == null) {
            return null;
        } else {
            return (TitanikEdge) edgeMap.get(outVertexId);
        }
    }

    @Override
    public void addOutEdge(TitanikEdge edge) {
        Map<Object, Edge> objectEdgeMap = outEdges.get(edge.getLabel());
        if (objectEdgeMap == null) {
            objectEdgeMap = Maps.newHashMap();
            outEdges.put(edge.getLabel(), objectEdgeMap);
        }
        objectEdgeMap.put(edge.getVertex(Direction.IN).getId(), edge);
    }

    @Override
    public void addInEdge(TitanikEdge edge) {
        Map<Object, Edge> objectEdgeMap = inEdges.get(edge.getLabel());
        if (objectEdgeMap == null) {
            objectEdgeMap = Maps.newHashMap();
            inEdges.put(edge.getLabel(), objectEdgeMap);
        }
        objectEdgeMap.put(edge.getVertex(Direction.OUT).getId(), edge);
    }

    @Override
    public void removeInEdge(Edge edge) {
        Map<Object, Edge> edgeMap = inEdges.get(edge.getLabel());
        Object vertexId = edge.getVertex(Direction.OUT).getId();
        edgeMap.remove(vertexId);
    }

    @Override
    public void removeOutEdge(Edge edge) {
        final Map<Object, Edge> edgeMap = outEdges.get(edge.getLabel());
        Object vertexId = edge.getVertex(Direction.IN).getId();
        edgeMap.remove(vertexId);
    }
}
