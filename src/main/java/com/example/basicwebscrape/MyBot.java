package com.example.basicwebscrape;

import java.io.*;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.vdurmont.emoji.EmojiParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import com.example.basicwebscrape.football.*;
import com.example.basicwebscrape.gold.*;
import com.example.basicwebscrape.news.NewsByTopic;
import com.example.basicwebscrape.weather.*;
import com.example.basicwebscrape.OilPrice.*;
public class MyBot extends TelegramLongPollingBot {
	static String topicNews;
	private GoldPrice crawler = new GoldPrice();
    @Override
    public void onUpdateReceived(Update update) {
        // TODO
        // We check if the update has a message and the message has text
        Boolean printedMany = false;
        //String chat_id = update.getMessage().getChatId().toString();
        if (update.hasMessage() && update.getMessage().hasText()) {
            String command = update.getMessage().getText();
            SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
            message.enableHtml(true);

            if(command.equals("/myname")){
                String msg = getBotUsername();
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText(msg);   
            }
            else if (command.equals("/start")){
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText("Menu");


                ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
                List<KeyboardRow> keyboard = new ArrayList<>();

                KeyboardRow row = new KeyboardRow();
                row.add("News and Topic");
                keyboard.add(row);
                
                row = new KeyboardRow();
                row.add("Weather Forecast");
                row.add("Football Events");
                keyboard.add(row);
                
                row = new KeyboardRow();

                row.add("Oil Price");
                row.add("Gold Price");
                keyboard.add(row);

                keyboardMarkup.setKeyboard(keyboard);
                message.setReplyMarkup(keyboardMarkup);
            }
            else if (command.equals("/weather") || command.equals("Weather Forecast")) {
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText("Select Forecast Duration");

                message.setReplyMarkup(Weather.setButtons());
            }
            //YOUR COMMAND HERE
            // NEWS

            else if (command.equals("/news") || command.equals("News and Topic")) {
            	message.setChatId(update.getMessage().getChatId().toString());
                message.setText("Select Topic");

                message.setReplyMarkup(NewsByTopic.setButtons());
            }

            // GOLD
            // OIL
            else if(command.equals("Gold Price")) {
            	String msg = "";
                List<String> IDs = new ArrayList<>(GoldPrice.urlMap.keySet());
               	for (String ID :IDs) {
               		msg += "* "+ID+" :\n"+GoldPrice.getPrice(ID) + "\n----------\n";
               	}
               	msg = EmojiParser.parseToUnicode(msg);
               	message.setChatId(update.getMessage().getChatId().toString());
                message.setText(msg);
                try {
					execute(message);
				} catch (TelegramApiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
               	msg = "Hãy nhập vào lệnh: \n"
            			+ ":information_source: Định dạng /gold:\n"
            			+ "/gold all\n"
            			+ "/gold key\n"
            			+ "/gold key history\n"
            			+ "\nkey:\n";
            	for (String k : GoldPrice.urlMap.keySet()) {
            		msg += k+"\n";
            	}
            	msg = EmojiParser.parseToUnicode(msg);
            	message.setChatId(update.getMessage().getChatId().toString());
                message.setText(msg);
               
            }
            else if(command.contains("/gold")){
            	
                String[] str = command.split(" ");
                String msg = "";

                if(str.length == 1) {
                     	
                	msg = ":information_source: Định dạng /gold:\n"
                			+ "/gold all\n"
                			+ "/gold key\n"
                			+ "/gold key history\n"
                			+ "\nkey:\n";
                	for (String k : GoldPrice.urlMap.keySet()) {
                		msg += k+"\n";
                	}
                	msg = EmojiParser.parseToUnicode(msg);
                }
                else {
                	msg += "Đơn vị (đồng/lượng)\n\n";
                	   if(str[1].equals("all")) {
	                       	List<String> IDs = new ArrayList<>(GoldPrice.urlMap.keySet());
	                       	for (String ID :IDs) {
	                       		msg += "* "+ID+" :\n"+GoldPrice.getPrice(ID) + "\n----------\n";
	                       	}
	                       	msg = EmojiParser.parseToUnicode(msg);

                       }else {
                  
	                       	if(!GoldPrice.dataMap.containsKey(str[1])) {
	                       		 msg = "ID không tồn tại";
	                       	}else {
	                       			if(str.length == 2) {
	                       				msg += GoldPrice.getPrice(str[1]);
	                       				msg = EmojiParser.parseToUnicode(msg);
	                       			}else if(str.length == 3) {
	                       				if(str[2].equals("history")) {
	                       					msg += GoldPrice.get_history(str[1]);
	                       					msg = EmojiParser.parseToUnicode(msg);
	                       				}else {
	                       				
	                       					msg = "Câu lệnh không hợp lệ";
	                       				}
	                       			}else {
	                       				
	                       				msg = "Câu lệnh không hợp lệ";
	                       			}
		       	                	
	                       	}
                       }
                		
                }
             
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText(msg);
                
            }
            // FOOTBALL
            else if (command.equals("/football") || command.equals("Football Events")) {
            	message.setChatId(update.getMessage().getChatId().toString());
            	message.setText("Chọn một trong các mục sau");
            	message.setReplyMarkup(Buttons.setButtons1());
            }

            //END QUERIES
            else if (command.equals("/hide")) {
                message.setText("Keyboard hidden");
                message.setChatId(update.getMessage().getChatId().toString());
                ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove(true);
                message.setReplyMarkup(keyboardMarkup);
            }
            //OIL
            else if(command.equals("/oilprice") || command.equals("Oil Price")){
                String url = "https://www.pvoil.com.vn/truyen-thong/tin-gia-xang-dau";
                String msg=OilPrice.returnOilPrice(url);
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText(msg);
            }
            else{
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText("Xin lỗi, câu lệnh của bạn không tồn tại");
            }
            if (!printedMany){
                try {
                    execute(message); // Call method to send the message
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (update.hasCallbackQuery()) {
            SendMessage message = new SendMessage();
            message.enableHtml(true);
            Message msg = update.getCallbackQuery().getMessage();
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            System.out.println(data);
            message.setChatId(msg.getChatId().toString());

            String topic = data.split("_")[0];
            String type = data.split("_")[1];

            // FOOTBALL
            String league = data.split("_")[0];
          	if (league.equals("no")) {
          		message.setText("Chọn giải đấu");
          		message.setReplyMarkup(Buttons.setButtons2(type));
          		try {
                    execute(message); // Call method to send the message
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
          	}
          	else {
	            if (type.equals("standing")) {
	            	message.setText(Standing.getMessage(league));
	                try {
	                    execute(message); // Call method to send the message
	                } catch (TelegramApiException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (type.equals("matches")) {
	                message.setText(Matches.getMessage(league));
	                try {
	                    execute(message); // Call method to send the message
	                } catch (TelegramApiException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (type.equals("scorers")) {
	                message.setText(Scorers.getMessage(league));
	                try {
	                    execute(message); // Call method to send the message
	                } catch (TelegramApiException e) {
	                    e.printStackTrace();
	                }
	            }
          	}

            // WEATHER CALLBACKS: DAILY AND HOURLY
            if (type.equals("daily")){
                SendPhoto photoDay = new SendPhoto();
                SendPhoto photoNight = new SendPhoto();
                photoDay.setChatId(msg.getChatId().toString());
                photoNight.setChatId(msg.getChatId().toString());
                TreeMap<String,ArrayList<String>> msgs = Weather.getForecastDaily(topic);
                
                for (HashMap.Entry<String,ArrayList<String>> m:msgs.entrySet()) {
                    String strr = m.getKey();
                    int first_under = strr.indexOf("Ngày");
                    int second_under = strr.indexOf("Đêm",first_under+1);
                    String messageOverall = strr.substring(0,first_under);
                    String messageDay = strr.substring(first_under,second_under);
                    String messageNight = strr.substring(second_under);

                    message.setText(messageOverall);
                    photoDay.setPhoto(new InputFile(new File(m.getValue().get(0)),"Day"));
                    photoNight.setPhoto(new InputFile(new File(m.getValue().get(1)),"Night"));
                    photoDay.setCaption(messageDay);
                    photoNight.setCaption(messageNight);

                    try {
                        execute(message);
                        execute(photoDay);
                        execute(photoNight);
                    } catch (TelegramApiException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                printedMany = true; 
            }
            if (type.equals("hourly")){
                SendPhoto photo = new SendPhoto();
                photo.setChatId(msg.getChatId().toString());
                message.setChatId(msg.getChatId().toString());
                TreeMap<String,String> msgs = Weather.getForecastHourly(topic);
                for (HashMap.Entry<String, String> m:msgs.entrySet()) {
                    String strr = m.getKey();
                    photo.setPhoto(new InputFile(new File(m.getValue()),"Hour"));
                    int breaker = strr.indexOf("Nhiệt độ");
                    message.setText(strr.substring(0,breaker));
                    photo.setCaption(strr.substring(breaker));
                    try {
                        execute(message);
                        execute(photo);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                printedMany = true; 

            }

            // OTHER CALLBACKS: ......
            if (topic.equals("news")) {
            	if (type.equals("next")) {
            		NewsByTopic news = new NewsByTopic(topicNews);
                	message.setChatId(msg.getChatId().toString());
                	message.setText(news.randomNews());
                	message.setReplyMarkup(NewsByTopic.setNext());
            	}
            	else if (type.equals("return")) {
            		message.setChatId(msg.getChatId().toString());
                    message.setText("Select Topic");
                    message.setReplyMarkup(NewsByTopic.setButtons());
            	}
            	else {
            		NewsByTopic news;
	            	if (type.equals("latest")) {
	            		news = new NewsByTopic();
	            		message.setChatId(msg.getChatId().toString());
		            	message.setText(news.getNewsByTopic());
	            	}
	            	else {
	            		news = new NewsByTopic(type);
	            		topicNews = type;
	            		message.setChatId(msg.getChatId().toString());
		            	message.setText(news.getNewsByTopic());
		            	message.setReplyMarkup(NewsByTopic.setNext());
	            	}
            	}
            	try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    

    @Override
    public String getBotUsername() {
        // TODO
        return "InfoBot";
    }

    @Override
    public String getBotToken() {
        // TODO
        return "5336710924:AAFeLLC8O7ScaBwNWv-o_Jo_jAIJbFl3WdY";
    }
}