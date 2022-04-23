package com.example.basicwebscrape;

import java.io.*;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import com.example.basicwebscrape.weather.*;
public class MyBot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        // TODO
        // We check if the update has a message and the message has text
        Boolean printedMany = false;
        String chat_id = update.getMessage().getChatId().toString();
        if (update.hasMessage() && update.getMessage().hasText()) {
            String command = update.getMessage().getText();
            SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
            message.enableHtml(true);
            if(command.equals("/myname")){
                String msg = getBotUsername();
                message.setChatId(chat_id);
                message.setText(msg);   
            }
            else if (command.equals("/weather")) {
                message.setChatId(chat_id);
                message.setText("Here is your keyboard");

                ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
                List<KeyboardRow> keyboard = new ArrayList<>();
                KeyboardRow row = new KeyboardRow();
                row.add("Get a 5-day forecast");
                keyboard.add(row);
                row = new KeyboardRow();
                row.add("Get a 12-hour forecast");
                keyboard.add(row);

                keyboardMarkup.setKeyboard(keyboard);
                message.setReplyMarkup(keyboardMarkup);
            }
            else if(command.contains("/weatherdaily") || command.equals("Get a 5-day forecast")){
                SendPhoto photoDay = new SendPhoto();
                SendPhoto photoNight = new SendPhoto();
                photoDay.setChatId(chat_id);
                photoNight.setChatId(chat_id);
                message.setChatId(chat_id);
                TreeMap<String,ArrayList<String>> msgs = new TreeMap<String,ArrayList<String>>();
                if(command.equals("/weatherdaily") || command.equals("/weatherdaily ") || command.equals("Get a 5-day forecast")){
                    msgs = Weather.getForecastDaily("hochiminh");
                }
                else{
                    String[] str = command.split(" ");
                    msgs = Weather.getForecastDaily(str[1]);
                }
                for (HashMap.Entry<String,ArrayList<String>> msg:msgs.entrySet()) {
                    String strr = msg.getKey();
                    int first_under = strr.indexOf("Ngày");
                    int second_under = strr.indexOf("Đêm",first_under+1);
                    String messageOverall = strr.substring(0,first_under);
                    String messageDay = strr.substring(first_under,second_under);
                    String messageNight = strr.substring(second_under);

                    message.setText(messageOverall);
                    photoDay.setPhoto(new InputFile(new File(msg.getValue().get(0)),"Day"));
                    photoNight.setPhoto(new InputFile(new File(msg.getValue().get(1)),"Night"));
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
            else if(command.contains("/weatherhourly") || command.equals("Get a 12-hour forecast")){
                SendPhoto photo = new SendPhoto();
                TreeMap<String,String> msgs = new TreeMap<String,String>();
                photo.setChatId(chat_id);
                message.setChatId(chat_id);
                if(command.equals("/weatherhourly") || command.equals("/weatherhourly ") || command.equals("Get a 12-hour forecast")){
                    msgs = Weather.getForecastHourly("hochiminh");
                }
                else{
                    String[] str = command.split(" ");
                    msgs = Weather.getForecastHourly(str[1]);
                }
                for (HashMap.Entry<String, String> msg:msgs.entrySet()) {
                    String strr = msg.getKey();
                    photo.setPhoto(new InputFile(new File(msg.getValue()),"Hour"));
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
            else if (command.equals("/hide")) {
                message.setText("Keyboard hidden");
                message.setChatId(chat_id);
                ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove(true);
                message.setReplyMarkup(keyboardMarkup);
            }
            else{
                message.setChatId(chat_id);
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
    }

    @Override
    public String getBotUsername() {
        // TODO
        return "saka12_bot";
    }

    @Override
    public String getBotToken() {
        // TODO
        return "5117983190:AAEXlcpIKO2tcMO7JKSu8CLkh350RRDuR3w";
    }
}
