package com.example.basicwebscrape.football;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Scorers {
	public static String getMessage(String league) {
		// league urls
    	Map<String, String> urls = new HashMap<String, String>();
    	urls.put("England", "https://www.fctables.com/england/premier-league/top-scorers/");
    	urls.put("Spain", "https://www.fctables.com/spain/liga-bbva/top-scorers/");
    	urls.put("Italy", "https://www.fctables.com/italy/serie-a/top-scorers/");
    	urls.put("Germany", "https://www.fctables.com/germany/1-bundesliga/top-scorers/");
    	urls.put("France", "https://www.fctables.com/france/ligue-1/top-scorers/");
    	urls.put("Vietnam", "https://www.fctables.com/vietnam/v-league/top-scorers/");
    	
    	// name league
    	Map<String, String >leagues = new HashMap<String, String>();
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
			int i = 10;
			Element all = document.select(
					"table.table.stage-table.table-striped.table-bordered.table-hover.table-condensed.responsive").select(
							"tbody").get(0);
			for (Element row : all.children()) {
				String rank = row.select("td:nth-of-type(1)").text();
				String player = row.select("td.tl:nth-of-type(2)").text();
				String club = row.select("td.tl:nth-of-type(3)").text();
				String goal = row.select(".bold").text();
				msg += rank + ". " + player + " (" + club + "): " + goal + "\n";
				i--;
				if (i == 0) break;
			}
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    	return msg;
    }
}
