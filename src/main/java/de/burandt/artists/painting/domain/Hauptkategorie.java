package de.burandt.artists.painting.domain;

public enum Hauptkategorie {
	CURRENT("current"),
	REPRESENTATIONAL("representational"),
	ABSTRACT("abstract"),
	EXHIBITION("exhibition"),
	COLLAGE("collage"),
	DRAWING("drawing"),
	ABOUT("about"),
	INDEX("index");
	
	private String hauptkategorie;
	
	Hauptkategorie(String hauptkategorie){
		this.hauptkategorie = hauptkategorie;
	}

	public String getHauptkategorie() {
		return hauptkategorie;
	}
	
}
