package com.example.basicwebscrape.football;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Standing {
	private Map<String, String> urls;
	private Map<String, String> leagues;
	
	public Standing() {
		// league urls
    	this.urls = new HashMap<String, String>();
    	urls.put("England", "https://www.livescores.com/football/england/premier-league/?tz=7");
    	urls.put("Spain", "https://www.livescores.com/football/spain/laliga-santander/?tz=7");
    	urls.put("Italy", "https://www.livescores.com/football/italy/serie-a/?tz=7");
    	urls.put("Germany", "https://www.livescores.com/football/germany/bundesliga/?tz=7");
    	urls.put("France", "https://www.livescores.com/football/france/ligue-1/?tz=7");
    	urls.put("Vietnam", "https://www.livescores.com/football/vietnam/v-league/?tz=7");
    	
    	// name league
    	this.leagues = new HashMap<String, String>();
    	leagues.put("England", "Premier League");
    	leagues.put("Spain", "Laliga Santader");
    	leagues.put("Italy", "Serie A");
    	leagues.put("Germany", "Bundesliga");
    	leagues.put("France", "Ligue 1");
    	leagues.put("Vietnam", "V-League");
	}
	
	public String getMessage(String league) {
		String url = urls.get(league);
		String name = leagues.get(league);
    	String msg = "";
    	try {
    		msg += name + "\n";
    		msg += "-------------------------\n";
    		final Document document = Jsoup.connect(url).get();
    		for (Element row : document.select(
    			"table.styled__Table-sc-1elgz3g-1.gsiGjM tr")) {
    			if (row.select(".rank").text().equals("")) {
    				continue;
    			}
    			else {
    				final String rank = row.select(".rank").text();
    				final String team = row.select(".LeagueRow_teamCell__2cmu7").text();
    				final String paras = row.select(".LeagueRow_centerCell__2n3eq").text();
    				//System.out.println(rank + ". " + team + "\n" + convert(paras));
    				msg += rank + ". " + team + "\n" + convert(paras) + "\n\n";
    			}
    		}
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    	return msg;
    }
	
	public String convert(String paras) {
    	String[] temp = paras.split(" ");
    	paras = "";
    	paras += "P:" + temp[0] + ", ";
    	paras += "W:" + temp[1] + ", ";
    	paras += "D:" + temp[2] + ", ";
    	paras += "L:" + temp[3] + ", ";
    	paras += "F:" + temp[4] + ", ";
    	paras += "A:" + temp[5] + ", ";
    	paras += "GD:" + temp[6] + ", ";
    	paras += "PTS:" + temp[7];
    	return paras;
    }
}
