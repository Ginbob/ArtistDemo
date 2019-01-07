package de.burandt.artists.painting.controller.wrapper;

import de.burandt.artists.painting.domain.Painting;

import java.util.List;

public class PaintingWrapper {
    private List<Painting> paintings;
    private String description;

    public PaintingWrapper() {}

    public PaintingWrapper(List<Painting> paintings) {
        this.paintings = paintings;
    }

    public List<Painting> getPaintings() {
        return paintings;
    }

    public void setPaintings(List<Painting> paintings) {
        this.paintings = paintings;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
