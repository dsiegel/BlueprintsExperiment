package com.tinkerpop.blueprints.util.wrappers.event2.listener;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.util.wrappers.event.listener.*;

/**
 * Event fired when an edge property is removed.
 *
 * @author Stephen Mallette
 */
public class EdgePropertyRemovedEvent extends EdgePropertyEvent {

    public EdgePropertyRemovedEvent(final Edge vertex, final String key, final Object oldValue) {
        super(vertex, key, oldValue, null);
    }

    @Override
    void fire(final GraphChangedListener listener, final Edge edge, final String key, final Object oldValue, final Object newValue) {
        listener.edgePropertyRemoved(edge, key, oldValue);
    }
}
