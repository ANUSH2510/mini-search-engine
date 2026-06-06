package com.minicrawl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.HttpStatusException;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;



@Service
public class Crawler {
    private final PageRepository pageRepository;
    private final Indexer indexer;
    private final RobotChecker robotChecker;
    private Set<String> visitedUrls=new HashSet<>();
    private static final int MAX_PAGES=20;

    public Crawler(PageRepository pageRepository , Indexer indexer, RobotChecker robotChecker) {
        this.pageRepository=pageRepository;
        this.indexer=indexer;
        this.robotChecker=robotChecker;
    }

    public void crawl(String seedUrl) {

        Queue<String> queue=new LinkedList<>();

        int pageCount=0;

        queue.add(seedUrl);
        if(visitedUrls.contains(seedUrl)) {
            return;
        }
        if(visitedUrls.size() >= MAX_PAGES) {
            return;
        }

        while(!queue.isEmpty()) {
            int attempts = 0;
            boolean success = false;

            if(pageCount >= 50) {
                System.out.println("page count = " + pageCount);
                break;
            }

            String url=queue.poll();
            if (visitedUrls.contains(url)) {
                continue;
            }
            while(attempts < 3 && !success) {
                try {
                    Thread.sleep(500);
                    if(!robotChecker.isAllowed(url)){
                        System.out.println("Blocked By Robots.txt:" + url);
                        continue;
                    }
                    Document doc = Jsoup.connect(url).timeout(5000).get();

                    String title = doc.title();
                    String text = doc.select("p").text();

                    Page page = new Page();
                    page.setUrl(url);
                    page.setTitle(title);
                    page.setContent(text);
                    page.setStatus("success");

                    if(pageRepository.findByUrl(url).isPresent()) {
                        continue;
                    }
                    pageRepository.save(page);
                    pageCount++;
                    visitedUrls.add(url);
                    System.out.println("Crawled pages = " + pageCount);

                    success = true;

                    System.out.println("Crawled: " + url);

                    int linkCount = 0;
                    for (Element link : doc.select("a[href]")) {
                        if (linkCount >= 50) break;
                        String absUrl = link.absUrl("href");
                        absUrl = absUrl.split("#")[0];
                        if (absUrl.startsWith("https://en.wikipedia.org/wiki/") && !absUrl.substring(30).contains(":") && !visitedUrls.contains(absUrl)) {
                            queue.add(absUrl);
                            linkCount++;
                        }
                    }
                }catch (HttpStatusException e) {
                    int statusCode = e.getStatusCode();
                    System.out.println("HTTP ERROR" + statusCode + "for" + url);

                    if (statusCode == 404) {
                        System.out.println("Skipping 404 pages: " + url);
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                } catch (Exception e) {

                    attempts++;
                    System.out.println("Retrying(" + attempts + "/3): " + url);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            if(!success) {
                Page page = new Page();
                page.setUrl(url);
                page.setStatus("error");

                pageRepository.save(page);

                System.out.println("Failed after 3 attempts: " + url);
            }
            visitedUrls.add(url);
        }
        indexer.indexPages();
    }
}
