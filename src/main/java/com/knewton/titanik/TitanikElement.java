package com.knewton.titanik;

import com.tinkerpop.blueprints.Element;

public interface TitanikElement extends Element {
    TitanikTransactionalGraph getGraph();
}
