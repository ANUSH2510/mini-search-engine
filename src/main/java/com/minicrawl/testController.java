package com.minicrawl;
import org.springframework.web.bind.annotation.*;
import java.util.List;

public class testController {
    private final PageRepository repo;

    public testController(PageRepository repo) {
        this.repo=repo;
    }
     @GetMapping("/add")
    public String add(){
        Page p=new Page();
        p.setUrl("https://example.com");
        p.setTitle("Example");
        p.setContent("Test content");

        repo.save(p);
        return "saved";
     }
     @GetMapping("/all")
    public List<Page> all(){
        return repo.findAll();
     }
}
