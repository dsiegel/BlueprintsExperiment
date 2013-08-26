package com.knewton.titanik;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;

public class TitanikTransactionEvent {
    public final Map<Object,TitanikVertex> newVertices;
    public final Set<Object> deletedVertices;
    public final Map<Object, TitanikEdge> newEdges;
    public final Set<Object> deletedEdges;

    public TitanikTransactionEvent() {
        newVertices = Maps.newHashMap();
        deletedVertices = Sets.newHashSet();
        newEdges = Maps.newHashMap();
        deletedEdges = Sets.newHashSet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TitanikTransactionEvent that = (TitanikTransactionEvent) o;

        if (!newVertices.equals(that.newVertices)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return newVertices.hashCode();
    }
}
