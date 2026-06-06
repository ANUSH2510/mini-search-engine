package com.minicrawl;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String url;
    private String title;

    @Column (columnDefinition = "TEXT")
    private String content;
    private String status;
    private LocalDateTime crawledAt;

    public Long getId() {return id; }

    public String geturl() {return url; }
    public void setUrl(String url) {this.url = url;}

    public String getTitle() {return title; }
    public void setTitle(String title) {this.title = title; }

    public String getContent() {return content; }
    public void setContent(String content) {this.content = content; }

    public String getStatus() {return status ;}
    public void setStatus(String status) {this.status = status ;}

    public LocalDateTime getCrawledAt() { return crawledAt;}
    public void setCrawledAt(LocalDateTime crawledAt) {
        this.crawledAt = crawledAt;
    }


}
