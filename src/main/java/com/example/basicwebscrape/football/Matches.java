package com.example.basicwebscrape.football;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Matches {
	private Map<String, String> urls;
	private Map<String, String> leagues;
	
	public Matches() {
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
			msg += "-----------------------------------------\n";
			final Document document = Jsoup.connect(url).get();
			Element all = document.select(".MatchRows_root__1NKae").get(0);
			for (Element row : all.children()) {
				if (row.className().equals("CategoryHeader_categoryHeaderWrapper__33fmX")) {
					String date = row.select(".CategoryHeader_date__4oEhd").text();
					//System.out.println(date);
					msg += date +"\n";
				}
				if (row.className().equals("MatchRow_footballRoot__1S4eG MatchRow_isHighlighted__397Dm")) {
					String time = row.select(".MatchRowTime_justifyContentStart__3XvwU.MatchRowTime_time__2Fkd2").text();
					String home = row.select(".FootballMatchRow_home__2jnn7").text();
					String score = row.select(".FootballMatchRow_score__2sJ4_").text();
					String away = row.select(".FootballMatchRow_away__12Br8").text();
					//System.out.println(time + ": " + home + " " + score + " " + away);
					msg += time + ": " + home + " " + score + " " + away + "\n\n";
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
    	return msg;
    }
}
