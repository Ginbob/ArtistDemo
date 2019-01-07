package de.burandt.artists.exhibition.domain;

import javax.persistence.*;

@Entity
public class ExhibitionPoster {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @OneToOne
    @JoinColumn(name = "exhibition")
    private Exhibition exhibition;
    private String filename;

    public ExhibitionPoster() {
    }

    public ExhibitionPoster(Exhibition exhibition, String filename) {
        this.exhibition = exhibition;
        this.filename = filename;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Exhibition getExhibition() {
        return exhibition;
    }

    public void setExhibition(Exhibition exhibition) {
        this.exhibition = exhibition;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExhibitionPoster)) return false;

        ExhibitionPoster that = (ExhibitionPoster) o;

        if (exhibition != null ? !exhibition.equals(that.exhibition) : that.exhibition != null) return false;
        return filename != null ? filename.equals(that.filename) : that.filename == null;
    }

    @Override
    public int hashCode() {
        int result = exhibition != null ? exhibition.hashCode() : 0;
        result = 31 * result + (filename != null ? filename.hashCode() : 0);
        return result;
    }
}
