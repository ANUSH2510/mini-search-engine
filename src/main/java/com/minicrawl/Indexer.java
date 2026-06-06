package com.minicrawl;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class Indexer {

    private final PageRepository pageRepository;
    private final IndexEntryRepository indexEntryRepository;

    private static final Set<String> STOP_WORDS = Set.of(
            "is" ,"a","the","in","on","of","to","and","for"
    );

    public Indexer(PageRepository pageRepository,IndexEntryRepository indexEntryRepository) {
        this.pageRepository=pageRepository;
        this.indexEntryRepository=indexEntryRepository;
    }

    public void indexPages() {
        System.out.println("INDEXER STARTED");
        List<Page> pages = pageRepository.findAll();
        indexEntryRepository.deleteAllInBatch();
        for(Page page : pages) {
            if(page.getContent() == null) continue;

            String content=page.getContent().toLowerCase();
            content=content.replaceAll("[^a-zA-Z0-9 ]", "");

            String [] words=content.split("\\s+");

            Map<String,Integer> frequency=new HashMap<>();
            for(String word : words) {
                if(word.isEmpty()) {
                    continue;
                }
                frequency.put(word,frequency.getOrDefault(word,0) + 1);
            }
            for(Map.Entry<String,Integer> entry : frequency.entrySet()) {
                IndexEntry indexEntry=new IndexEntry();

                indexEntry.setWord(entry.getKey());
                indexEntry.setFrequency(entry.getValue());
                indexEntry.setPage(page);

                boolean exists = indexEntryRepository.existsByWordAndPage(entry.getKey(), page);
                if(!exists) {
                    indexEntryRepository.save(indexEntry);
                }
            }
            System.out.println("Indexed pages:" + page.getId());
        }
    }
}
