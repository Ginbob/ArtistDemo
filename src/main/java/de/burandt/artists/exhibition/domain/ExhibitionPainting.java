package de.burandt.artists.exhibition.domain;

import javax.persistence.*;

@Entity
@Table(name = "exhibition_painting")
public class ExhibitionPainting {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String datei;
    @ManyToOne(optional = false)
    @JoinColumn(name = "exhibition")
    private Exhibition exhibition;

    public ExhibitionPainting() {}

    public ExhibitionPainting(String datei, Exhibition exhibition) {
        this.datei = datei;
        this.exhibition = exhibition;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDatei() {
        return datei;
    }

    public void setDatei(String datei) {
        this.datei = datei;
    }

    public Exhibition getExhibition() {
        return exhibition;
    }

    public void setExhibition(Exhibition exhibition) {
        this.exhibition = exhibition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExhibitionPainting)) return false;

        ExhibitionPainting that = (ExhibitionPainting) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (datei != null ? !datei.equals(that.datei) : that.datei != null) return false;
        return exhibition != null ? exhibition.equals(that.exhibition) : that.exhibition == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (datei != null ? datei.hashCode() : 0);
        result = 31 * result + (exhibition != null ? exhibition.hashCode() : 0);
        return result;
    }
}
