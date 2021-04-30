package net.trustly.scraper.domain;


public class PageItem {
    private String filename;

    private String extension;

    private boolean folder;

    private String uri;

    public PageItem(String filename, String uri) {
        this.filename = filename;
        this.uri = uri;
        int index = filename.lastIndexOf('.');
        this.extension = (index >= 0) ? filename.substring(index + 1) : "no_extension";
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getExtension() {
        return this.extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public boolean isFolder() {
        return this.folder;
    }

    public void setFolder(boolean folder) {
        this.folder = folder;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}

