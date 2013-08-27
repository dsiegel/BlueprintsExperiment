package com.tinkerpop.blueprints.util.wrappers.event2.listener;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.wrappers.event.listener.*;
import com.tinkerpop.blueprints.util.wrappers.event.listener.GraphChangedListener;

import java.util.Iterator;

/**
 * Base class for property changed events.
 *
 * @author Stephen Mallette
 */
public abstract class VertexPropertyEvent implements Event {

    private final Vertex vertex;
    private final String key;
    private final Object oldValue;
    private final Object newValue;

    public VertexPropertyEvent(final Vertex vertex, final String key, final Object oldValue, final Object newValue) {

        this.vertex = vertex;
        this.key = key;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    abstract void fire(final com.tinkerpop.blueprints.util.wrappers.event2.listener.GraphChangedListener listener, final Vertex vertex, final String key, final Object oldValue, final Object newValue);

    @Override
    public void fireEvent(final Iterator<com.tinkerpop.blueprints.util.wrappers.event2.listener.GraphChangedListener> eventListeners) {
        while (eventListeners.hasNext()) {
            fire(eventListeners.next(), vertex, key, oldValue, newValue);
        }
    }
}
