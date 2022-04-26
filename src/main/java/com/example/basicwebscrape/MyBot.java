package com.example.basicwebscrape;
import java.io.PrintStream;


import java.io.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import java.io.*;
import org.json.JSONArray;
import org.json.JSONObject;


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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import com.example.basicwebscrape.football.*;


import com.vdurmont.emoji.EmojiParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import com.example.basicwebscrape.gold.GoldPrice;
import com.example.basicwebscrape.weather.*;

public class MyBot extends TelegramLongPollingBot {
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
            // GOLD
            // OIL
            // FOOTBALL
			else if (command.equals("/matches")) {
            	message.setChatId(update.getMessage().getChatId().toString());
            	message.setText("Chọn giải đấu");
            	Buttons buttons = new Buttons();
				message.setReplyMarkup(buttons.setButtons("matches"));
            }
            else if (command.equals("/standing")) {
            	message.setChatId(update.getMessage().getChatId().toString());
            	message.setText("Chọn giải đấu");
            	Buttons buttons = new Buttons();
				message.setReplyMarkup(buttons.setButtons("standing"));
            }
            else if (command.equals("/scorers")) {
            	message.setChatId(update.getMessage().getChatId().toString());
            	message.setText("Chọn giải đấu");
            	Buttons buttons = new Buttons();
				message.setReplyMarkup(buttons.setButtons("scorers"));
            }
            //END QUERIES
            else if (command.equals("/hide")) {
                message.setText("Keyboard hidden");
                message.setChatId(update.getMessage().getChatId().toString());
                ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove(true);
                message.setReplyMarkup(keyboardMarkup);
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
            message.setChatId(msg.getChatId().toString());
            Standing standing = new Standing();
            Matches matches = new Matches();
            Scorers scorers = new Scorers();
            String topic = data.split("_")[0];
            String type = data.split("_")[1];
            if (type.equals("standing")) {
                message.setText(standing.getMessage(topic));
            }
            if (type.equals("matches")) {
                message.setText(matches.getMessage(topic));
            }
            if (type.equals("scorers")) {
                message.setText(scorers.getMessage(topic));
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
                try {
                    execute(message); // Call method to send the message
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
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
        }
    }
    
    @Override
    public String getBotUsername() {
        // TODO
        return "infoBot";
    }

    @Override
    public String getBotToken() {
        // TODO
        return "5124630324:AAGd47oSNGFSX2xhEdOmY8-G8BMD0v9rYDc";
    }
}
