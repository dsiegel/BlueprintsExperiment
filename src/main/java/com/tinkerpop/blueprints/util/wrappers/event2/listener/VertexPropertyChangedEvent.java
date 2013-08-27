package com.tinkerpop.blueprints.util.wrappers.event2.listener;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.wrappers.event.listener.*;

/**
 * Event that fires when a property changes on a vertex.
 *
 * @author Stephen Mallette
 */
public class VertexPropertyChangedEvent extends VertexPropertyEvent {

    public VertexPropertyChangedEvent(final Vertex vertex, final String key, final Object oldValue, final Object newValue) {
        super(vertex, key, oldValue, newValue);
    }

    @Override
    void fire(final GraphChangedListener listener, final Vertex vertex, final String key, final Object oldValue, final Object newValue) {
        listener.vertexPropertyChanged(vertex, key, oldValue, newValue);
    }
}
