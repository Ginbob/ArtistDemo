package de.burandt.artists.about.domain;

public class AboutWrapper {

    private About about;

    public AboutWrapper() {
    }

    public AboutWrapper(About about) {
        this.about = about;
    }

    public About getAbout() {
        return about;
    }

    public void setAbout(About about) {
        this.about = about;
    }
}
