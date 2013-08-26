package com.knewton.titanik;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.ElementHelper;

import java.util.Map;
import java.util.Set;

public abstract class AbstractNewTitanikElement implements TitanikElement {
    private final Object id;
    private final TitanikTransactionalGraph graph;
    private final Map<String, Object> newProperties = Maps.newHashMap();

    public AbstractNewTitanikElement(Object id, TitanikTransactionalGraph graph) {
        this.id = id;
        this.graph = graph;
    }

    @Override
    public Object getId() {
        return id;
    }

    @Override
    public <T> T getProperty(String key) {
        return (T) newProperties.get(key);
    }

    @Override
    public Set<String> getPropertyKeys() {
        return Sets.newHashSet(newProperties.keySet());
    }

    @Override
    public void setProperty(String key, Object value) {
        ElementHelper.validateProperty(this, key, value);
        newProperties.put(key, value);
    }

    @Override
    public <T> T removeProperty(String key) {
        return (T) newProperties.remove(key);
    }

    @Override
    public TitanikTransactionalGraph getGraph() {
        return graph;
    }
}
