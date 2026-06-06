package com.minicrawl;

public class SearchResult {
    private String title;
    private String url;
    private int frequency;
    private String snippet;

    public SearchResult(String title, String url, String snippet, int frequency) {
        this.title = title;
        this.url = url;
        this.snippet = snippet;
        this.frequency = frequency;

    }
    public String getTitle() {
        return title;
    }
    public String getUrl() {
        return url;
    }
    public String getSnippet() {return snippet;}
    public int getFrequency() {return frequency;}

}
