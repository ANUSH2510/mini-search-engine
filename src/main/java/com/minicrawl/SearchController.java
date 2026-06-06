package com.minicrawl;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import jakarta.persistence.Index;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class SearchController {
    private final IndexEntryRepository indexEntryRepository;

    public SearchController(IndexEntryRepository indexEntryRepository) {
        this.indexEntryRepository=indexEntryRepository;
    }
    @GetMapping("/")
    public String home() {
        return "index";
    }
    @GetMapping("/search-page")
    public String searchPage(@RequestParam String q,Model model) {
        List<SearchResult> results=search(q);
        model.addAttribute("results",results);
        model.addAttribute("query",q);
        return "index";
    }

    @ResponseBody
    @GetMapping("/search")
    public List<SearchResult> search(@RequestParam String q) {
        List<SearchResult> pages = new ArrayList<>();
        String [] words=q.toLowerCase().split(" ");

        Set<String> stopWords=Set.of("the" , "is" , "in" , "on" , "to" , "and","for","a","an","of");

        Map<Page, Integer> pageScores = new HashMap<>();
        Map<Page, Integer> matchedWords = new HashMap<>();

        for(String word:words){
            if(stopWords.contains(word)) {
                continue;
            }
            List<IndexEntry> results=indexEntryRepository.findByWordOrderByFrequencyDesc(word);

            for(IndexEntry indexEntry:results){
                Page page=indexEntry.getPage();

                int score=indexEntry.getFrequency();
                if(page.getTitle().toLowerCase().contains(word)) {
                    score+=50;
                }
                pageScores.put(page,pageScores.getOrDefault(page,0)+ score);

                matchedWords.put(page,matchedWords.getOrDefault(page,0)+ 1);
            }
        }
        List<Map.Entry<Page, Integer>>sortedPages=new ArrayList<>(pageScores.entrySet());

        sortedPages.sort((a ,b) ->b.getValue().compareTo(a.getValue()));

        for(Map.Entry<Page, Integer> entry:sortedPages){
            Page page = entry.getKey();
            if(matchedWords.get(page) == words.length) {
                String content = page.getContent();
                String snippet = "";
                int index = content.toLowerCase().indexOf(q.toLowerCase());
                if(index != -1) {
                    int start=Math.max(0,index - 50);
                    int end= Math.min(content.length(),index + 100);

                    snippet = content.substring(start,end);
                }
                else{
                    snippet = content.substring(0,Math.min(150,content.length()));
                }
                String highlightedSnippet=snippet;
                for(String word:words){
                    highlightedSnippet=highlightedSnippet.replaceAll("(?i)" + word,"<b>"+ word+"</b>");
                }
                SearchResult result=new SearchResult(page.getTitle(),page.geturl(),highlightedSnippet, entry.getValue());
                pages.add(result);
            }
        }
        return pages;
    }
}
