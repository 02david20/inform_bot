package com.example.basicwebscrape.weather;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONObject;

public class WeatherHourly {
    private String date;
    private double temp;
    private int prec;
    private String desc;
    public WeatherHourly(JSONObject o){
        Long time = o.getLong("EpochDateTime");
        Date datee = new Date(time * 1000L);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Jakarta")));
        int tem = o.getJSONObject("Temperature").getInt("Value");
        
        this.date = format.format(datee);
        this.temp = Math.round((tem-32.0)*5/9*100.0)/100.0;
        this.prec = o.getInt("PrecipitationProbability");
        this.desc = o.getString("IconPhrase");
    }
    public String getDate(){
        return this.date;
    }
    public void setDate(String date){
        this.date = date;
    }
    public double getTemp(){
        return this.temp;
    }
    public void setTemp(int temp){
        this.temp = temp;
    }
    public String getDesc(){
        return this.desc;
    }
    public void setDesc(String desc){
        this.desc = desc;
    }
    public int getPrec(){
        return this.prec;
    }
    public void setPrec(int prec){
        this.prec = prec;
    }
    public String toString(){
        //concat the result
        String result = 
            "--" + this.date + "--\n"
            + "Nhiệt độ: " + this.temp
            + " độ C\nDự báo: " + this.desc
            + "\nKhả năng có mưa: " + this.prec + "%\n\n";
        return result;   
    }
}
