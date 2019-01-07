package de.burandt.artists.painting.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Painting {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String name;
	private Integer entstehungsjahr;
	private Double hoehe;
	private Double breite;
	private String technik;
	private String datei;
	private String hauptkategorie;
	private String unterkategorie;
	private boolean markedAsDeleted;

	public Painting() {}
	
	public Painting(String name, Integer entstehungsjahr, Double hoehe, Double breite, String technik, String datei, String hauptkategorie, String unterkategorie, boolean markedAsDeleted) {
		this.name = name;
		this.entstehungsjahr = entstehungsjahr;
		this.hoehe = hoehe;
		this.breite = breite;
		this.technik = technik;
		this.datei = datei;
		this.hauptkategorie = hauptkategorie;
		this.unterkategorie = unterkategorie;
		this.markedAsDeleted = markedAsDeleted;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getEntstehungsjahr() {
		return entstehungsjahr;
	}

	public void setEntstehungsjahr(Integer entstehungsjahr) {
		this.entstehungsjahr = entstehungsjahr;
	}

	public Double getHoehe() {
		return hoehe;
	}

	public void setHoehe(Double hoehe) {
		this.hoehe = hoehe;
	}

	public Double getBreite() {
		return breite;
	}

	public void setBreite(Double breite) {
		this.breite = breite;
	}

	public String getTechnik() {
		return technik;
	}

	public void setTechnik(String technik) {
		this.technik = technik;
	}

	public String getDatei() {
		return datei;
	}

	public void setDatei(String datei) {
		this.datei = datei;
	}

	public String getHauptkategorie() {
		return hauptkategorie;
	}

	public void setHauptkategorie(String hauptkategorie) {
		this.hauptkategorie = hauptkategorie;
	}

	public String getUnterkategorie() {
		return unterkategorie;
	}

	public void setUnterkategorie(String unterkategorie) {
		this.unterkategorie = unterkategorie;
	}

	public boolean isMarkedAsDeleted() {
		return markedAsDeleted;
	}

	public void setMarkedAsDeleted(boolean markedAsDeleted) {
		this.markedAsDeleted = markedAsDeleted;
	}
}
