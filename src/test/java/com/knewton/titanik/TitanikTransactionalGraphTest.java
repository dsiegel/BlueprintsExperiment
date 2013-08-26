package com.knewton.titanik;

import com.google.common.collect.Sets;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TitanikTransactionalGraphTest {

    private Graph innerGraph;
    private TitanikTransactionalGraph underTest;
    private TitanikEventConsumer consumer;

    @Before
    public void before() {
        innerGraph = new TinkerGraph();
        consumer = mock(TitanikEventConsumer.class);
        underTest = new TitanikTransactionalGraph(innerGraph, consumer);
    }

    @Test
    public void testAddVertex() throws Exception {
        assertNull(underTest.getVertex(0));
        assertThat(underTest.getVertices(), emptyIterable());
        Vertex vertex = underTest.addVertex(0);
        assertEquals(0, vertex.getId());
        assertEquals(Sets.newHashSet(), vertex.getPropertyKeys());
        underTest.commit();
        TitanikTransactionEvent tx = new TitanikTransactionEvent();
        tx.newVertices.put(0, (TitanikVertex) vertex);
        verify(consumer).consume(eq(tx));
    }

    @Test
    public void testAddTwoVertex() throws Exception {
        Vertex v1 = underTest.addVertex(0);
        Vertex v2 = underTest.addVertex(1);
        assertEquals(0, v1.getId());
        assertEquals(1, v2.getId());
        underTest.commit();
        TitanikTransactionEvent tx = new TitanikTransactionEvent();
        tx.newVertices.put(0, (TitanikVertex) v1);
        tx.newVertices.put(1, (TitanikVertex) v2);
        verify(consumer).consume(eq(tx));
    }

    @Test
    public void testGetVertex() throws Exception {

    }

    @Test
    public void testGetVertices() throws Exception {

    }

    @Test
    public void testCommit() throws Exception {

    }

    @Test
    public void testShutdown() throws Exception {

    }

    @Test
    public void testRollback() throws Exception {

    }

    @Test
    public void testGetFeatures() throws Exception {

    }

    @Test
    public void testRemoveVertex() throws Exception {

    }

    @Test
    public void testAddEdge() throws Exception {

    }

    @Test
    public void testGetEdge() throws Exception {

    }

    @Test
    public void testRemoveEdge() throws Exception {

    }

    @Test
    public void testGetEdges() throws Exception {

    }

    @Test
    public void testQuery() throws Exception {

    }

    @Test
    public void testStopTransaction() throws Exception {

    }
}
