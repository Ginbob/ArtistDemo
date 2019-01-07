package de.burandt.artists.index.controller.wrapper;

import de.burandt.artists.index.domain.Index;

public class IndexWrapper {

    private Index index;

    public IndexWrapper() {
    }

    public IndexWrapper(Index index) {
        this.index = index;
    }

    public Index getIndex() {
        return index;
    }

    public void setIndex(Index index) {
        this.index = index;
    }
}
