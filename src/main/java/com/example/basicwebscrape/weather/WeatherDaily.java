package com.example.basicwebscrape.weather;

import java.util.ArrayList;
import org.json.JSONObject;
class Temperature{
    private double minVal;
    private double maxVal;
    
    public Temperature(){
        this.minVal = 0;
        this.maxVal = 0;
    }
    public Temperature(double minVal, double maxVal){
        this.minVal = Math.round((minVal - 32)*5/9 * 100.0)/100.0;
        this.maxVal = Math.round((maxVal - 32)*5/9 * 100.0)/100.0;
    }
    public void setMinTemp(int val){
        this.minVal = val;
    }
    public double getMinTemp(){
        return this.minVal;
    }
    public void setMaxTemp(int val){
        this.maxVal = val;
    }
    public double getMaxTemp(){
        return this.maxVal;
    }
    public String toString(){
        return String.valueOf(this.minVal) + " - " + String.valueOf(this.maxVal);
    }
}
class Time{
    private String desc;
    private Precipitation prec;
    public class Precipitation{
        private Boolean hasPrec;
        private ArrayList<String> ar;

        public Precipitation(){
            this.hasPrec = false;
            ar = new ArrayList<String>();
        }
        public Precipitation(String type, String intense){
            this.hasPrec = true;
            ar = new ArrayList<String>();
            this.ar.add(type);
            this.ar.add(intense);
        }
        public Boolean isPrec(){
            return this.hasPrec;
        }
        public ArrayList<String> getPrec(){
            if (this.hasPrec)
                return this.ar;
            else return null;
        }
        public void setPrec(String type, String intense){
            this.ar.set(0,type);
            this.ar.set(1,intense);
        }
    }
    public Time(){
        this.desc = "";
        this.prec = new Precipitation();
    }
    public Time(JSONObject o){
        this.desc = o.getString("IconPhrase");
        Boolean isPrec = o.getBoolean("HasPrecipitation");
        if (isPrec){
            String type = o.getString("PrecipitationType");
            String intensity = o.getString("PrecipitationIntensity");
            this.prec = new Precipitation(type,intensity);
        }
        else
            this.prec = new Precipitation();
    }
    public String getDesc(){
        return this.desc;
    }
    public void setDesc(String desc){
        this.desc = desc;
    }
    public ArrayList<String> getPrecipitation(){
        return this.prec.getPrec();
    }
    public void setPrecipitation(String type, String intense){
        this.prec.setPrec(type,intense);
    }
}
class Day extends Time{
    private int info;
    public Day(){
        this.info = -1;
    }
    
    public Day(JSONObject o){
        super(o);
        this.info = o.getInt("Icon");
    }
    public int getInfo(){
        return this.info;
    }
    public void setInfo(int info){
        this.info = info;
    }
    
    public String handleIconLink(){
        String pre = "images/";
        return pre + this.info +"-s.png";
    }
}
class Night extends Time{
    private int info;
    public Night(){
        this.info = -1;
    }
    public Night(JSONObject o){
        super(o);
        this.info = o.getInt("Icon");
    }
    public int getInfo(){
        return this.info;
    }
    public void setInfo(int info){
        this.info = info;
    }
    public String handleIconLink(){
        String pre = "images/";
        return pre + this.info +"-s.png";
    }
}
public class WeatherDaily {
    private String date;
    private Temperature temp;
    private Day day;
    private Night night;
    public WeatherDaily(){}
    public WeatherDaily(JSONObject o){
        String fullDate = o.getString("Date");
        JSONObject tem = o.getJSONObject("Temperature");
        int minTemp = tem.getJSONObject("Minimum").getInt("Value");
        int maxTemp = tem.getJSONObject("Maximum").getInt("Value");
        JSONObject dayObject = o.getJSONObject("Day");
        JSONObject nightObject = o.getJSONObject("Night");

        this.date = fullDate.substring(0,10);
        this.temp = new Temperature(minTemp, maxTemp);
        this.day = new Day(dayObject);
        this.night = new Night(nightObject);
    }
    
    public String getDate(){
        return this.date;
    }
    public void setDate(String date){
        this.date = date;
    }
    public Day getDay(){
        return this.day;
    }
    public void setDay(Day day){
        this.day = day;
    }
    public Night getNight(){
        return this.night;
    }
    public void setNight(Night night){
        this.night = night;
    }
    public String getDayIcon(){
        return this.day.handleIconLink();
    }
    public String getNightIcon(){
        return this.night.handleIconLink();
    }
    public String toString(){
        //concat the result
        String result = 
            "<b>" + this.date + "</b>\n"
            + "Bi??n ????? nhi???t: " + this.temp.toString() + " ????? C\n";
        if(this.day.getPrecipitation()!=null){
            result += " Ng??y: "
                    + "\n\tM?? t???: " + this.day.getDesc()
                    + "\n\tKh?? t?????ng: " + this.day.getPrecipitation().get(0)
                    + "\n\tC?????ng ?????: " + this.day.getPrecipitation().get(1)+"\n";
        }
        else{
            result += " Ng??y: "
                    + "\n\tM?? t???: " + this.day.getDesc()+"\n";
        }

        if(this.night.getPrecipitation()!=null){
            result += " ????m: "
                    + "\n\tM?? t???: " + this.night.getDesc()
                    + "\n\tKh?? t?????ng: " + this.night.getPrecipitation().get(0)
                    + "\n\tC?????ng ?????: " + this.night.getPrecipitation().get(1)+"\n\n";
        }
        else{
            result += " ????m: "
                    + "\n\tM?? t???: " + this.night.getDesc()+"\n\n";
        }
        return result;   
    }
}