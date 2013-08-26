package com.knewton.titanik;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public interface TitanikVertex extends Vertex, TitanikElement {

    TitanikEdge getOutEdge(String label, Object inVertexId);

    TitanikEdge getInEdge(String label, Object outVertexId);

    void addOutEdge(TitanikEdge edge);

    void addInEdge(TitanikEdge edge);

    void removeInEdge(Edge edge);

    void removeOutEdge(Edge edge);
}
