package com.example.basicwebscrape.football;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Matches {
	public static String getMessage(String league) {
		// league urls
		Map<String, String> urls = new HashMap<String, String>();
		urls.put("England", "https://www.livescores.com/football/england/premier-league/?tz=7");
		urls.put("Spain", "https://www.livescores.com/football/spain/laliga-santander/?tz=7");
		urls.put("Italy", "https://www.livescores.com/football/italy/serie-a/?tz=7");
		urls.put("Germany", "https://www.livescores.com/football/germany/bundesliga/?tz=7");
		urls.put("France", "https://www.livescores.com/football/france/ligue-1/?tz=7");
		urls.put("Vietnam", "https://www.livescores.com/football/vietnam/v-league/?tz=7");

		// name league
		Map<String, String> leagues = new HashMap<String, String>();
		leagues.put("England", "Premier League");
		leagues.put("Spain", "Laliga Santader");
		leagues.put("Italy", "Serie A");
		leagues.put("Germany", "Bundesliga");
		leagues.put("France", "Ligue 1");
		leagues.put("Vietnam", "V-League");

		String url = urls.get(league);
		String name = leagues.get(league);
    	String msg = "";
    	try {
			msg += name + "\n";
			msg += "-----------------------------------------\n";
			final Document document = Jsoup.connect(url).get();
			Element all = document.select(".Ea").get(0);
			for (Element row : all.children()) {
				if (row.className().equals("eb")) {
					String date = row.select(".fb").text();
					//System.out.println(date);
					msg += date +"\n";
				}
				if (row.className().equals("Jc Nc")) {
					String time = row.select(".qg.ug").text();
					String home = row.select(".Lh").text();
					String score = row.select(".Ih").text();
					String away = row.select(".Mh").text();
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
