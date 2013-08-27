package com.knewton.titanik;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

public class DirectConsumer implements TitanikEventConsumer {
    private final Graph graph;

    public DirectConsumer(Graph graph) {
        this.graph = graph;
    }

    @Override
    public void consume(TitanikTransactionEvent titanikTransactionEvent) {
        try {
            for (TitanikVertex titanikVertex : titanikTransactionEvent.newVertices.values()) {
                if (titanikVertex instanceof NewTitanikVertex) {
                    processNewVertex((NewTitanikVertex)titanikVertex);
                } else if (titanikVertex instanceof PersistedTitanikVertex) {
                    processPersistedVertex((PersistedTitanikVertex)titanikVertex);
                } else {
                    throw new RuntimeException("Invalid vertex type");
                }
            }

            // TODO(dsiegel): process edges

            //graph.commit();
        } catch (Exception e) {
            //graph.rollback();
            throw new RuntimeException("Failed to process transaction", e);
        }
    }

    private void processPersistedVertex(PersistedTitanikVertex vertex) {
        Vertex persistedVertex = graph.getVertex(vertex.getId());
        for (String key : vertex.getPropertyKeys()) {
            persistedVertex.setProperty(key, vertex.getProperty(key));
        }
    }

    private void processNewVertex(NewTitanikVertex vertex) {
        Vertex newVertex = graph.addVertex(vertex.getId());
        for (String key : vertex.getPropertyKeys()) {
            newVertex.setProperty(key, vertex.getProperty(key));
        }
    }
}
