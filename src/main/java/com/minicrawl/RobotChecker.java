package com.minicrawl;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RobotChecker {
    public boolean isAllowed(String url) {
        try{
            String domain=new java.net.URL(url).getProtocol() + "://"+ new java.net.URL(url).getHost();

            String robotsUrl = domain + "/robots.txt";
            String robotsText= Jsoup.connect(robotsUrl).ignoreContentType(true).execute().body();

            String path = new java.net.URL(url).getPath();
            String [] lines=robotsText.split("\n");

            for (String line:lines){
                if(line.startsWith("Disallow: /wiki/")) {
                    String disallowpath = line.replace("Disallow:", "").trim();

                    if(!disallowpath.isEmpty() && path.startsWith(disallowpath)){
                    return false;
                    }
                }
            }
            return true;

        }catch(IOException e){
            return true;
        }
    }

}
