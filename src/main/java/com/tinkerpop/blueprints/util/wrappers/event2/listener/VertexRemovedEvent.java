package com.tinkerpop.blueprints.util.wrappers.event2.listener;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.wrappers.event.listener.*;
import com.tinkerpop.blueprints.util.wrappers.event.listener.GraphChangedListener;

import java.util.Iterator;
import java.util.Map;

/**
 * Event fired when a vertex is removed.
 *
 * @author Stephen Mallette
 */
public class VertexRemovedEvent implements Event {

    private final Vertex vertex;
    private final Map<String, Object> props;

    public VertexRemovedEvent(final Vertex vertex, final Map<String, Object> props) {
        this.vertex = vertex;
        this.props = props;
    }

    @Override
    public void fireEvent(final Iterator<com.tinkerpop.blueprints.util.wrappers.event2.listener.GraphChangedListener> eventListeners) {
        while (eventListeners.hasNext()) {
            eventListeners.next().vertexRemoved(vertex, props);
        }
    }
}
