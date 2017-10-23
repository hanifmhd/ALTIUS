package com.mis.altius.adapter;

/**
 * Created by Hanifmhd on 9/12/2017.
 */

public class SearchResult {
    private String title;
    private String description;
    private String iconUrl;

    public SearchResult(String title, String description, String iconUrl) {
        this.title = title;
        this.description = description;
        this.iconUrl = iconUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}