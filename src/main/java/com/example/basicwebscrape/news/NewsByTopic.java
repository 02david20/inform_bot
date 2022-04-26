package com.example.basicwebscrape.news;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

class Topic {
	private String url;
	
	public Topic() {
		url = "https://thanhnien.vn/";
	}
	public Topic(String topic) {
		this.url = "https://thanhnien.vn/" + topic.replace(' ', '-') + "/";
	}
	
	public String getUrl() {
		return this.url;
	}
}

public class NewsByTopic {
	private Topic topic;
	
	public NewsByTopic() {
		this.topic = new Topic();
	}
	public NewsByTopic(String topic) {
		this.topic = new Topic(topic);
	}
	
	// Function to crawl news from https://thanhnien.vn/
	public String getNewsByTopic() {
		Document doc = null;
		Elements lstPost = null;
        try {
            doc = Jsoup
                    .connect(topic.getUrl())
                    .userAgent("Jsoup client")
                    .timeout(20000).get();
            lstPost = doc.select("a.story__thumb");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lstPost.attr("href");
    }
}