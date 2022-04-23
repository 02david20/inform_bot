package com.example.basicwebscrape;
import java.io.PrintStream;


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
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import com.vdurmont.emoji.EmojiParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.example.basicwebscrape.weather.*;

public class MyBot extends TelegramLongPollingBot {
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    //private final String apikey = "gHuEn9ghiy20CHSHAJ4ccgWcdU0XWkGS";
    private final String apikey = "dhJQfH709c5McTPTTa2ZfF9WCfCuwNPl";
    private static GoldPrice crawler = new GoldPrice();
    private static HashMap<String,Integer> cities;
    static {
        cities = new HashMap<String, Integer>();
        cities.put("hanoi", 353412);
        cities.put("danang", 352954);
        cities.put("hochiminh", 352981);
    }
    public TreeMap<String,ArrayList<String>> getForecastDaily(String cityName) {
        if (cityName != null && !cityName.isBlank()) {
            try {
                //handle to find city
                cityName = cityName.toLowerCase();
                String cityKey = Integer.toString(cities.get(cityName));
                String url = "https://dataservice.accuweather.com/forecasts/v1/daily/5day/" + cityKey;
                String query = "?apikey=" + apikey +"&language=vi";
                HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(url+query)).build();
                CompletableFuture<HttpResponse<String>> response = httpClient.sendAsync(httpRequest,HttpResponse.BodyHandlers.ofString());
                String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
                JSONObject myObj = new JSONObject(result);
                JSONArray fore = myObj.getJSONArray("DailyForecasts");
                WeatherDaily[] days = new WeatherDaily[5];
                HashMap<String,ArrayList<String>> ret = new HashMap<String,ArrayList<String>>();
                for(int i=0;i<5;i++){
                    JSONObject day = fore.getJSONObject(i);
                    days[i] = new WeatherDaily(day);
                    ArrayList<String> l = new ArrayList<String>();
                    l.add(days[i].getDayIcon());
                    l.add(days[i].getNightIcon());
                    ret.put(days[i].toString(),l);
                }
                // TreeMap to store values of HashMap
                TreeMap<String, ArrayList<String>> sorted = new TreeMap<>();
 
                // Copy all data from hashMap into TreeMap
                sorted.putAll(ret);
                return sorted;
            }
            catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
        else {
            return null;
        }
    }
    public TreeMap<String,String> getForecastHourly(String cityName) {
        if (cityName != null && !cityName.isBlank()) {
            try {
                //handle to find city
                cityName = cityName.toLowerCase();
                String cityKey = Integer.toString(cities.get(cityName));
                String url = "https://dataservice.accuweather.com/forecasts/v1/hourly/12hour/" + cityKey;
                String query = "?apikey=" + apikey +"&language=vi";
                HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(url+query)).build();
                CompletableFuture<HttpResponse<String>> response = httpClient.sendAsync(httpRequest,HttpResponse.BodyHandlers.ofString());
                String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
                JSONArray fore = new JSONArray(result);
                WeatherHourly[] hours = new WeatherHourly[12];
                HashMap<String,String> ret = new HashMap<String,String>();
                for(int i=0;i<12;i++){
                    JSONObject hour = fore.getJSONObject(i);
                    hours[i] = new WeatherHourly(hour);
                    ret.put(hours[i].toString(),hours[i].getIcon());
                }
                // TreeMap to store values of HashMap
                TreeMap<String, String> sorted = new TreeMap<>();
 
                // Copy all data from hashMap into TreeMap
                sorted.putAll(ret);
                return sorted;
            }
            catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
        else {
            return null;
        }
    }
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
            else if(command.contains("/weatherdaily") || command.equals("Get a 5-day forecast")){
                SendPhoto photoDay = new SendPhoto();
                SendPhoto photoNight = new SendPhoto();
                photoDay.setChatId(chat_id);
                photoNight.setChatId(chat_id);
                message.setChatId(chat_id);
                TreeMap<String,ArrayList<String>> msgs = new TreeMap<String,ArrayList<String>>();
                if(command.equals("/weatherdaily") || command.equals("/weatherdaily ")){
                    msgs = getForecastDaily("hochiminh");
                }
                else{
                    String[] str = command.split(" ");
                    msgs = getForecastDaily(str[1]);
                }
                for (HashMap.Entry<String,ArrayList<String>> msg:msgs.entrySet()) {
                    String strr = msg.getKey();
                    int first_under = strr.indexOf("NgÃ y");
                    int second_under = strr.indexOf("Ä�Ãªm",first_under+1);
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
                try {
                    execute(message); // Call method to send the message
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if(command.contains("/gold")){
            	
                String[] str = command.split(" ");
                String msg = "";

                if(str.length == 1) {
                	msg = ":information_source: Định dạng /gold:\n"
                			+ "/gold all\n"
                			+ "/gold key\n"
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
		       	                	msg = GoldPrice.getPrice(str[1]);
		       	                	msg = EmojiParser.parseToUnicode(msg);
	                       	}
                       }
                		
                }
             
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText(msg);
            }
            
            else if(command.contains("/weatherhourly") || command.equals("Get a 12-hour forecast")){
                SendPhoto photo = new SendPhoto();
                TreeMap<String,String> msgs = new TreeMap<String,String>();
                photo.setChatId(chat_id);
                message.setChatId(chat_id);
                if(command.equals("/weatherhourly") || command.equals("/weatherhourly ")){
                    msgs = getForecastHourly("hochiminh");
                }
                else{
                    String[] str = command.split(" ");
                    msgs = getForecastHourly(str[1]);
                }
                for (HashMap.Entry<String, String> msg:msgs.entrySet()) {
                    String strr = msg.getKey();
                    photo.setPhoto(new InputFile(new File(msg.getValue()),"Hour"));
                    int breaker = strr.indexOf("Nhiá»‡t Ä‘á»™");
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

            else if (command.equals("/markup")) {
                message.setChatId(chat_id);
                message.setText("Here is your keyboard");
                // Create ReplyKeyboardMarkup object
                ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
                // Create the keyboard (list of keyboard rows)
                List<KeyboardRow> keyboard = new ArrayList<>();
                // Create a keyboard row
                KeyboardRow row = new KeyboardRow();
                // Set each button, you can also use KeyboardButton objects if you need something else than text
                row.add("Get a 5-day forecast");
                row.add("Get a 12-hour forecast");
                // Add the first row to the keyboard
                keyboard.add(row);
                // Set the keyboard to the markup
                keyboardMarkup.setKeyboard(keyboard);
                // Add it to the message
                message.setReplyMarkup(keyboardMarkup);
                try {
                    execute(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            else if (command.equals("/hide")) {
                message.setText("Keyboard hidden");
                message.setChatId(chat_id);
                ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove();
                message.setReplyMarkup(keyboardMarkup);
                try {
                    execute(message); // Call method to send the photo
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
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
    
    public String getGoldPrice() {
    
        return null;
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
