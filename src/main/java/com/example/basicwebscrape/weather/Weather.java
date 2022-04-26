package com.example.basicwebscrape.weather;

import java.net.URI;
import java.net.http.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.json.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class Weather {
    private static HashMap<String,Integer> cities;
    private static final String apikey = "gHuEn9ghiy20CHSHAJ4ccgWcdU0XWkGS";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    //private final String apikey = "dhJQfH709c5McTPTTa2ZfF9WCfCuwNPl";
    static {
        cities = new HashMap<String, Integer>();
        cities.put("hanoi", 353412);
        cities.put("danang", 352954);
        cities.put("hochiminh", 352981);
    }
    public static TreeMap<String,ArrayList<String>> getForecastDaily(String cityName) {
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
    public static TreeMap<String,String> getForecastHourly(String cityName) {
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
    
    public static InlineKeyboardMarkup setButtons() {
    	InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		List < List < InlineKeyboardButton >> buttons = new ArrayList<>();
		List < InlineKeyboardButton > button_list1 = new ArrayList<>();
		List < InlineKeyboardButton > button_list2 = new ArrayList<>();
		List < InlineKeyboardButton > button_list3 = new ArrayList<>();

		InlineKeyboardButton hn1 = new InlineKeyboardButton();
		hn1.setText("Hà Nội - 12 giờ");
		hn1.setCallbackData("hanoi_hourly");
		InlineKeyboardButton hn2 = new InlineKeyboardButton();
		hn2.setText("Hà Nội - 5 ngày");
		hn2.setCallbackData("hanoi_daily");

		InlineKeyboardButton dn1 = new InlineKeyboardButton();
        dn1.setText("Đà Nẵng - 12 giờ");
		dn1.setCallbackData("danang_hourly");
		InlineKeyboardButton dn2 = new InlineKeyboardButton();
		dn2.setText("Đà Nẵng - 5 ngày");
		dn2.setCallbackData("danang_daily");

		InlineKeyboardButton hcm1 = new InlineKeyboardButton();
		hcm1.setText("TP.HCM - 12 giờ");
		hcm1.setCallbackData("hochiminh_hourly");
		InlineKeyboardButton hcm2 = new InlineKeyboardButton();
		hcm2.setText("TP.HCM - 5 ngày");
		hcm2.setCallbackData("hochiminh_daily");
		
		
		button_list1.add(hn1);
		button_list1.add(hn2);
		button_list2.add(dn1);
		button_list2.add(dn2);
		button_list3.add(hcm1);
		button_list3.add(hcm2);
		
		buttons.add(button_list1);
		buttons.add(button_list2);
		buttons.add(button_list3);
		markupInline.setKeyboard(buttons);
		
		return markupInline;
	}
}
