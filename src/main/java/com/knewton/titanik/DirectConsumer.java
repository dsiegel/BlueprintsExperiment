package com.knewton.titanik;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

public class DirectConsumer implements TitanikEventConsumer {
    private final Graph graph;

    public DirectConsumer(Graph graph) {
        this.graph = graph;
    }

    @Override
    public void consume(TitanikTransactionEvent tx) {
        for (TitanikVertex vertex : tx.newVertices.values()) {
            Vertex newVertex = graph.addVertex(vertex.getId());
            for (String key : vertex.getPropertyKeys()) {
                newVertex.setProperty(key, vertex.getProperty(key));
            }
        }
    }
}
