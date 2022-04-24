package gold;

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
    private static ArrayList<String> tracking;
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
    	
    	tracking = new ArrayList<String>();
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
			// If the date crawled is today then just return
    
			if(today.updateTime.equals(Today())) {
				return to_string(ID);
			}
    	}
    	crawl_data(ID);
    	return to_string(ID);
    }
    
    private static String to_string(String ID) {
    	ArrayList<OneDay>data = dataMap.get(ID);
    	OneDay temp = data.get(data.size()-1);
    	OneDay yesterday = data.get(data.size()-2);
    	int sell_change = temp.sell-yesterday.sell;
    	int buy_change = temp.buy-yesterday.buy;

    	String str = "Date : "+ temp.date
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
    
    public static String get_history(String ID) {
    	ArrayList<OneDay>data = dataMap.get(ID);
    	if(data.size() == 0 && !data.get(data.size()-1).updateTime.equals(Today())) {
    		crawl_data(ID);
    		data = dataMap.get(ID);
    	}
    	String str = new String();
    	OneDay temp = data.get(0);
    	str += "Date: "+ temp.date
				+"\n\tBuy: "+temp.buy+" "
				+"\n\tSell: "+temp.sell+" "+"\n";
    	
    	for (int i = 1; i < data.size() ; i++) {
    		OneDay today = data.get(i);
    	 	OneDay yesterday = data.get(i-1);
        	int sell_change = today.sell-yesterday.sell;
        	int buy_change = today.buy-yesterday.buy;
        	str += "Date : "+ today.date
				+"\n\tBuy: "+today.buy+" "+change(buy_change)
				+"\n\tSell: "+today.sell+" "+change(sell_change)+"\n";

    	}
    	return str;
    }
    // Tracking Gold every day
    public Boolean Track_ID(String ID) {
    	if(! dataMap.containsKey(ID)) {
    		return false;
    	}
    	if(tracking.contains(ID))
    		return true;
    	tracking.add(ID);
    	return true;
    }
    
    public String getTrackInfo() {
    	String msg = new String();
    	for (String ID : tracking) {
    		msg += getPrice(ID) + "\n----------\n";
    	}
    	return msg;
    }
    
    private static String Today() {
    	Date date= new Date();
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		df.setTimeZone(TimeZone.getTimeZone("Asia/VietNam"));
		return df.format(date);
    }
}
