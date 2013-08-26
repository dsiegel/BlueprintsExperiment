package com.knewton.titanik;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tinkerpop.blueprints.Element;

import java.util.Map;
import java.util.Set;

public abstract class AbstractPersistedTitanikElement implements TitanikElement {
    private final TitanikTransactionalGraph graph;
    private final Element persistedElement;
    private final Map<String, Object> newProperties;
    private final Set<String> deletedProperties;

    public AbstractPersistedTitanikElement(TitanikTransactionalGraph graph, Element persistedElement) {
        this.graph = graph;
        this.persistedElement = persistedElement;
        newProperties = Maps.newHashMap();
        deletedProperties = Sets.newHashSet();
    }

    @Override
    public Object getId() {
        return persistedElement.getId();
    }

    @Override
    public <T> T getProperty(String key) {
        if (deletedProperties.contains(key)) {
            return null;
        } else {
            T property = (T) newProperties.get(key);
            if (property == null) {
                return persistedElement.getProperty(key);
            } else {
                return property;
            }
        }
    }

    @Override
    public Set<String> getPropertyKeys() {
        return ImmutableSet.<String>builder()
                .addAll(newProperties.keySet())
                .addAll(persistedElement.getPropertyKeys())
                .build();
    }

    @Override
    public void setProperty(String key, Object value) {
        newProperties.put(key, value);
    }

    @Override
    public <T> T removeProperty(String key) {
        T property = getProperty(key);
        newProperties.remove(key);
        deletedProperties.add(key);
        return property;
    }

    @Override
    public TitanikTransactionalGraph getGraph() {
        return graph;
    }
}
