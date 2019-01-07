package de.burandt.artists.exhibition.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
public class Exhibition {

	@Id
	private String id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate;
	private String title;
	private String exhibitor;
	private String location;
	private String openingTimes;
	private String vernissage;
	private String description;
	private boolean currentFuture;
	private String link;
	@OneToMany(mappedBy = "exhibition", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private List<ExhibitionPainting> paintings;
	@OneToOne(mappedBy = "exhibition", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private ExhibitionPoster poster;

    public Exhibition() {
    }

    public Exhibition(Date startDate, Date endDate, String title, String exhibitor, String location, String openingTimes, String vernissage, String link, String description) {
        this.id = UUID.randomUUID().toString();
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.exhibitor = exhibitor;
        this.location = location;
        this.openingTimes = openingTimes;
        this.vernissage = vernissage;
        this.currentFuture = Date.from(Instant.now()).before(endDate);
        this.link = link;
        this.description = description;
    }

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCurrentFuture() {
        return currentFuture;
    }

    public void setCurrentFuture(boolean currentFuture) {
        this.currentFuture = currentFuture;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean hasLink() {
        return this.link != null && !this.link.equals("https://") && !this.link.isEmpty();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean hasDescription() {
        return this.description != null && !this.description.isEmpty();
    }

    public List<ExhibitionPainting> getPaintings() {
        return paintings;
    }

    public void setPaintings(List<ExhibitionPainting> paintings) {
        this.paintings = paintings;
    }

    public ExhibitionPoster getPoster() {
        return poster;
    }

    public void setPoster(ExhibitionPoster poster) {
        this.poster = poster;
    }

    public String getExhibitor() {
        return exhibitor;
    }

    public void setExhibitor(String exhibitor) {
        this.exhibitor = exhibitor;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOpeningTimes() {
        return openingTimes;
    }

    public void setOpeningTimes(String openingTimes) {
        this.openingTimes = openingTimes;
    }

    public String getVernissage() {
        return vernissage;
    }

    public void setVernissage(String vernissage) {
        this.vernissage = vernissage;
    }
}