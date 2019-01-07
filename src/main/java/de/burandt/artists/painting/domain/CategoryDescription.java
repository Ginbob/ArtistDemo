package de.burandt.artists.painting.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CategoryDescription {

    @Id
    public String category;

    public String descriptionTeaser;
    public String descriptionMore;


    public CategoryDescription() {}

    public CategoryDescription(String category, String descriptionTeaser, String descriptionMore) {
        this.category = category;
        this.descriptionTeaser = descriptionTeaser;
        this.descriptionMore = descriptionMore;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescriptionTeaser() {
        return descriptionTeaser;
    }

    public void setDescriptionTeaser(String descriptionTeaser) {
        this.descriptionTeaser = descriptionTeaser;
    }

    public String getDescriptionMore() {
        return descriptionMore;
    }

    public void setDescriptionMore(String descriptionMore) {
        this.descriptionMore = descriptionMore;
    }

    public boolean isEmpty() {
        return this.descriptionTeaser.isEmpty() && this.descriptionMore.isEmpty();
    }
}
