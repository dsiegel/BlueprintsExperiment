package com.tinkerpop.blueprints.util.wrappers.event2.listener;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.wrappers.event.listener.*;
import com.tinkerpop.blueprints.util.wrappers.event.listener.GraphChangedListener;

/**
 * Event fired when a vertex property is removed.
 *
 * @author Stephen Mallette
 */
public class VertexPropertyRemovedEvent extends VertexPropertyEvent {

    public VertexPropertyRemovedEvent(final Vertex vertex, final String key, final Object removedValue) {
        super(vertex, key, removedValue, null);
    }

    @Override
    void fire(final com.tinkerpop.blueprints.util.wrappers.event2.listener.GraphChangedListener listener, final Vertex vertex, final String key, final Object oldValue, final Object newValue) {
        listener.vertexPropertyRemoved(vertex, key, oldValue);
    }
}
