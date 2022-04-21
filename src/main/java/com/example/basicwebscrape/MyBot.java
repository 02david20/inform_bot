package com.example.basicwebscrape;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.example.basicwebscrape.weather.*;
public class MyBot extends TelegramLongPollingBot {
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private final String apikey = "gHuEn9ghiy20CHSHAJ4ccgWcdU0XWkGS";
    //dhJQfH709c5McTPTTa2ZfF9WCfCuwNPl
    private static HashMap<String,Integer> cities;
    static {
        cities = new HashMap<String, Integer>();
        cities.put("hanoi", 353412);
        cities.put("danang", 352954);
        cities.put("hochiminh", 352981);
    }
    public String getForecastDaily(String cityName) {
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
                String ret = "";
                for(int i=0;i<5;i++){
                    JSONObject day = fore.getJSONObject(i);
                    days[i] = new WeatherDaily(day);
                    ret+=days[i].toString();
                }
                return ret;
            }
            catch (Exception ex) {
                ex.printStackTrace();
                return "Có lỗi xảy ra";
            }
        }
        else {
            return "Vui lòng nhập tên thành phố !";
        }
    }
    public String getForecastHourly(String cityName) {
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
                String ret = "";
                for(int i=0;i<12;i++){
                    JSONObject hour = fore.getJSONObject(i);
                    hours[i] = new WeatherHourly(hour);
                    ret+=hours[i].toString();
                }
                return ret;
            }
            catch (Exception ex) {
                ex.printStackTrace();
                return "Có lỗi xảy ra";
            }
        }
        else {
            return "Vui lòng nhập tên thành phố !";
        }
    }
    public String getIDLocation(String location){
        return "";
    }
    @Override
    public void onUpdateReceived(Update update) {
        // TODO
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            String command = update.getMessage().getText();
            SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
            
            if(command.equals("/myname")){
                String msg = getBotUsername();
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText(msg);
            }
            else if(command.contains("/weatherdaily")){
                String[] str = command.split(" ");
                String msg = getForecastDaily(str[1]);
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText(msg);
            }
            else if(command.contains("/weatherhourly")){
                String[] str = command.split(" ");
                String msg = getForecastHourly(str[1]);
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText(msg);
            }
            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
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
