package com.minicrawl;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MinicrawlApplication implements CommandLineRunner{

	private final Crawler crawler;

	public MinicrawlApplication(Crawler crawler) {
		this.crawler = crawler;
	}

	public static void main(String[] args) {
		SpringApplication.run(MinicrawlApplication.class , args);
	}

	@Override
	public void run(String... args) throws Exception {
		crawler.crawl("https://en.wikipedia.org/wiki/Java_(programming_language)");
	}
}
