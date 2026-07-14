package com.minicrawl;

public class SearchResult {
    private String title;
    private String url;
    private double score;
    private String snippet;

    public SearchResult(String title, String url, String snippet, double score) {
        this.title = title;
        this.url = url;
        this.snippet = snippet;
        this.score = score;

    }
    public String getTitle() {
        return title;
    }
    public String getUrl() {
        return url;
    }
    public String getSnippet() {return snippet;}
    public double getScore() {return score;}

}
