package com.example.basicwebscrape;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;

class OneDay {
	public int sell;
	public int buy;
	public String date;
	public String updateTime;
	public OneDay(int sell,int buy, String date,String updateTime) {
		this.sell = sell;
		this.buy = buy;
		this.date = date;
		this.updateTime = updateTime;
	}
	
	@Override
	public String toString() {
		return "Date: " + date + ", Sell: "+ sell+", Buy: "+buy;
	}
}

public class GoldPrice {
	
	private boolean crawled ;
	
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    public static Map <String, String> urlMap;
    public static Map <String, ArrayList<OneDay> > dataMap;
    public GoldPrice() {
    	
    	urlMap = new HashMap <String,String>();
    	urlMap.put("DOJI_HN", "https://vtc.vn/AddOn/GoldPriceForChart?name=DOJI%20HN");
    	urlMap.put("DOJI_HCM","https://vtc.vn/AddOn/GoldPriceForChart?name=DOJI%20SG");
    	urlMap.put("MTB", "https://vtc.vn/AddOn/GoldPriceForChart?name=MARITIME%20BANK");
    	urlMap.put("PQ_SJC", "https://vtc.vn/AddOn/GoldPriceForChart?name=Ph%C3%BA%20Q%C3%BAy%20SJC");
    	urlMap.put("SJC_HCM", "https://vtc.vn/AddOn/GoldPriceForChart?name=SJC%20TP%20HCM");
    	urlMap.put("SJC_HN", "https://vtc.vn/AddOn/GoldPriceForChart?name=SJC%20H%C3%A0%20N%E1%BB%99i");
    	urlMap.put("SJC_DN", "https://vtc.vn/AddOn/GoldPriceForChart?name=SJC%20%C4%90%C3%A0%20N%E1%BA%B5ng");
    	urlMap.put("PNJ_HCM", "https://vtc.vn/AddOn/GoldPriceForChart?name=PNJ%20TP.HCM");
    	urlMap.put("PNJ_HN", "https://vtc.vn/AddOn/GoldPriceForChart?name=PNJ%20HN");
    	
    	dataMap = new HashMap <String,ArrayList<OneDay>>();
    	dataMap.put("DOJI_HN", new ArrayList<OneDay>());
    	dataMap.put("DOJI_HCM",new ArrayList<OneDay>());
    	dataMap.put("MTB", new ArrayList<OneDay>());
    	dataMap.put("PQ_SJC", new ArrayList<OneDay>());
    	dataMap.put("SJC_HCM", new ArrayList<OneDay>());
    	dataMap.put("SJC_HN", new ArrayList<OneDay>());
    	dataMap.put("SJC_DN", new ArrayList<OneDay>());
    	dataMap.put("PNJ_HCM", new ArrayList<OneDay>());
    	dataMap.put("PNJ_HN", new ArrayList<OneDay>());
    	
    	this.crawled = false;
    }
    public static void crawl_data(String ID) {
    	String urlString = urlMap.get(ID);
    	try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlString))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();
            JSONObject jsonObject = new JSONObject(body);

            JSONArray _dateArray = jsonObject.toJSONArray(jsonObject.names());
            JSONArray dateArray = (JSONArray)_dateArray.get(0);
            parse(ID,dateArray);
        }
        catch (Exception ex) {
            System.out.println("There is errors occur");
        }
    }
    private static void parse(String ID,JSONArray responseBody) {
    	ArrayList<OneDay> Data = new ArrayList<OneDay>() ;
    	JSONArray albums = responseBody;
    	for (int i = 0 ; i < albums.length(); i++) {
    		JSONObject album = albums.getJSONObject(i);
    		String Date = album.getString("DatePrice");
    		int Sell = album.getInt("Sell");
    		int Buy = album.getInt("Buy");
    		Date date= new Date();
    		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    		df.setTimeZone(TimeZone.getTimeZone("Asia/VietNam"));
    		Data.add(new OneDay(Sell,Buy,Date,df.format(date)));
    	}
    	dataMap.put(ID, Data);
    } 
    
    
    public static String getPrice(String ID) {
    	
    	List<OneDay> data = dataMap.get(ID);
    	if(data.size() != 0) {
    		OneDay today = data.get(data.size()-1);
	    	Date date= new Date();
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			df.setTimeZone(TimeZone.getTimeZone("Asia/VietNam"));
			// If the date crawled is today then just return
			if(today.updateTime == df.format(date)) {
				return to_string(ID);
			}
    	}
    	crawl_data(ID);
    	return to_string(ID);
    }
    
    public static String to_string(String ID) {
    	List<String> l = new ArrayList<String>();
    	ArrayList<OneDay>data = dataMap.get(ID);
    	OneDay temp = data.get(data.size()-1);
    	OneDay yesterday = data.get(data.size()-2);
    	int sell_change = temp.sell-yesterday.sell;
    	int buy_change = temp.buy-yesterday.buy;

    	String str = "Date: "+ temp.date
    				+"\n\tBuy: "+temp.buy+" "+change(buy_change)
    				+"\n\tSell: "+temp.sell+" "+change(sell_change);
    	return str;
    	
    }
    
    private static String change(int change) {
    	if(change > 0) {
    		return ":arrow_up: " + Integer.toString(change);
    	}
    	else if(change < 0) {
    		return ":small_red_triangle_down: " + Integer.toString(-change);
    	}
    	else {
    		return ":arrow_right: Not changed";
    	}
    	
    }
    
    
}
