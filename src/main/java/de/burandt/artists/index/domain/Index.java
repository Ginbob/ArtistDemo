package de.burandt.artists.index.domain;

import javax.persistence.*;

@Entity
@Table(name = "index_table")
public class Index {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String headline;
    private String paragraph;
    private String imageFileName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getParagraph() {
        return paragraph;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }
}
