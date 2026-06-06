package com.minicrawl;
import jakarta.persistence.*;

@Entity
@Table(name="index_entries")
public class IndexEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String word;
    private int frequency;

    @ManyToOne
    @JoinColumn(name = "page_id")
    private Page page;

    public Page getPage() { return page;}

    public void setPage(Page page) { this.page = page;}

    public Long getId(){return id; }
    public void setId(Long Id) {this.id=Id;}

    public String getWord(){return word;}
    public void setWord(String word) {this.word= word ;}

    public int getFrequency() {return frequency;}
    public void setFrequency(int frequency){this.frequency=frequency;}
}
