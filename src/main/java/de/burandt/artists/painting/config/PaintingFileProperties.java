package de.burandt.artists.painting.config;

import de.burandt.artists.painting.domain.Hauptkategorie;
import de.burandt.artists.painting.exception.InvalidCategoryException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "painting-file-pathes")
public class PaintingFileProperties {

    private String representational;
    private String abstractpath;
    private String exhibition;
    private String current;
    private String collage;
    private String drawing;
    private String about;
    private String index;

    public String getPathForCategory(String category) throws InvalidCategoryException {
        switch (Hauptkategorie.valueOf(category.toUpperCase())) {
            case REPRESENTATIONAL:
                return getRepresentational();
            case ABSTRACT:
                return getAbstractpath();
            case CURRENT:
                return getCurrent();
            case DRAWING:
                return getDrawing();
            case COLLAGE:
                return getCollage();
            default:
                throw new InvalidCategoryException("Ungültige oder noch nicht im Service eingepflegte Hauptkategorie: " + category);
        }
    }

    public String getRepresentational() {
        return representational;
    }

    public void setRepresentational(String representational) {
        this.representational = representational;
    }

    public String getAbstractpath() {
        return abstractpath;
    }

    public void setAbstractpath(String abstractpath) {
        this.abstractpath = abstractpath;
    }

    public String getExhibition() {
        return exhibition;
    }

    public void setExhibition(String exhibition) {
        this.exhibition = exhibition;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getCollage() {
        return collage;
    }

    public void setCollage(String collage) {
        this.collage = collage;
    }

    public String getDrawing() {
        return drawing;
    }

    public void setDrawing(String drawing) {
        this.drawing = drawing;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
