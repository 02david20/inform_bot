package com.example.basicwebscrape.news;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

class Topic {
	private String url;	
	public Topic() {
		this.url = "https://thanhnien.vn/";
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
	
	public String randomNews() {
		Document doc = null;
		Elements lstPost = null;
		try {
            doc = Jsoup
                    .connect(this.topic.getUrl())
                    .userAgent("Jsoup client")
                    .timeout(20000).get();
            lstPost = doc.select("div.relative article a.story__thumb");
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		Random rand = new Random();
		Element randomPost = lstPost.get(rand.nextInt(lstPost.size()));
		return randomPost.attr("href");
	}
	
	public static InlineKeyboardMarkup setNext() {
		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		List < List < InlineKeyboardButton >> buttons = new ArrayList<>();
		List < InlineKeyboardButton > button_list = new ArrayList<>();
		
		InlineKeyboardButton nextButton = new InlineKeyboardButton();
		nextButton.setText("Tiếp tục");
		nextButton.setCallbackData("news_next");
		InlineKeyboardButton returnButton = new InlineKeyboardButton();
		returnButton.setText("Trở lại");
		returnButton.setCallbackData("news_return");
		
		button_list.add(nextButton);
		button_list.add(returnButton);
		buttons.add(button_list);		
		markupInline.setKeyboard(buttons);
		return markupInline;
	}
	
	public static InlineKeyboardMarkup setButtons() {
		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		List < List < InlineKeyboardButton >> buttons = new ArrayList<>();
		List < InlineKeyboardButton > button_list = new ArrayList<>();
		List < InlineKeyboardButton > button_list1 = new ArrayList<>();
		List < InlineKeyboardButton > button_list2 = new ArrayList<>();
		List < InlineKeyboardButton > button_list3 = new ArrayList<>();
		
		InlineKeyboardButton latestNews = new InlineKeyboardButton();
		latestNews.setText("Bài mới nhất");
		latestNews.setCallbackData("news_latest");

		InlineKeyboardButton topic1 = new InlineKeyboardButton();
		topic1.setText("Video");
		topic1.setCallbackData("news_video");
		InlineKeyboardButton topic2 = new InlineKeyboardButton();
		topic2.setText("Thời sự");
		topic2.setCallbackData("news_thoi-su");
		InlineKeyboardButton topic3 = new InlineKeyboardButton();
		topic3.setText("Thế giới");
		topic3.setCallbackData("news_the-gioi");
		InlineKeyboardButton topic4 = new InlineKeyboardButton();
		topic4.setText("Tài chính");
		topic4.setCallbackData("news_tai-chinh-kinh-doanh");
		InlineKeyboardButton topic5 = new InlineKeyboardButton();
		topic5.setText("Đời sống");
		topic5.setCallbackData("news_doi-song");
		
		InlineKeyboardButton topic6 = new InlineKeyboardButton();
		topic6.setText("Văn hóa");
		topic6.setCallbackData("news_van-hoa");
		InlineKeyboardButton topic7 = new InlineKeyboardButton();
		topic7.setText("Giải trí");
		topic7.setCallbackData("news_giai-tri");
		InlineKeyboardButton topic8 = new InlineKeyboardButton();
		topic8.setText("Giới trẻ");
		topic8.setCallbackData("news_gioi-tre");
		InlineKeyboardButton topic9 = new InlineKeyboardButton();
		topic9.setText("Giáo dục");
		topic9.setCallbackData("news_giao-duc");
		InlineKeyboardButton topic10 = new InlineKeyboardButton();
		topic10.setText("Thể thao");
		topic10.setCallbackData("news_the-thao");
		
		InlineKeyboardButton topic11 = new InlineKeyboardButton();
		topic11.setText("Sức khỏe");
		topic11.setCallbackData("news_suc-khoe");
		InlineKeyboardButton topic12 = new InlineKeyboardButton();
		topic12.setText("Công nghệ");
		topic12.setCallbackData("news_cong-nghe-game");
		InlineKeyboardButton topic13 = new InlineKeyboardButton();
		topic13.setText("Xe");
		topic13.setCallbackData("news_xe");
		InlineKeyboardButton topic14 = new InlineKeyboardButton();
		topic14.setText("Bạn đọc");
		topic14.setCallbackData("news_ban-doc");
		
		button_list.add(latestNews);
		
		button_list1.add(topic1);
		button_list1.add(topic2);
		button_list1.add(topic3);
		button_list1.add(topic4);
		
		button_list2.add(topic5);		
		button_list2.add(topic6);
		button_list2.add(topic7);
		button_list2.add(topic8);
		button_list2.add(topic9);
		
		button_list3.add(topic10);		
		button_list3.add(topic11);
		button_list3.add(topic12);
		button_list3.add(topic13);
		button_list3.add(topic14);
		
		buttons.add(button_list);
		buttons.add(button_list1);
		buttons.add(button_list2);
		buttons.add(button_list3);
		markupInline.setKeyboard(buttons);
		
		return markupInline;
	}
}