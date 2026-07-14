package com.minicrawl;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IndexEntryRepository extends JpaRepository<IndexEntry ,Long>{
    List<IndexEntry> findByWordOrderByFrequencyDesc(String word);
    boolean existsByWordAndPage(String word,Page page);
    long countByWord(String word);
}
