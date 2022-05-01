package com.example.basicwebscrape.football;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class Buttons {
	public static InlineKeyboardMarkup setButtons1() {
		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		List < List < InlineKeyboardButton >> buttons = new ArrayList<>();
		List < InlineKeyboardButton > button_list1 = new ArrayList<>();
		List < InlineKeyboardButton > button_list2 = new ArrayList<>();
		List < InlineKeyboardButton > button_list3 = new ArrayList<>();
		
		InlineKeyboardButton standing = new InlineKeyboardButton();
		standing.setText("Bảng xếp hạng");
		standing.setCallbackData("no_standing");
		InlineKeyboardButton matches = new InlineKeyboardButton();
		matches.setText("Các trận đấu gần nhất");
		matches.setCallbackData("no_matches");
		InlineKeyboardButton scorers = new InlineKeyboardButton();
		scorers.setText("Top 10 vua phá lưới giải đấu");
		scorers.setCallbackData("no_scorers");
		
		button_list1.add(standing);
		button_list2.add(matches);
		button_list3.add(scorers);
		
		buttons.add(button_list1);
		buttons.add(button_list2);
		buttons.add(button_list3);
		markupInline.setKeyboard(buttons);
		return markupInline;
	}
	
	public static InlineKeyboardMarkup setButtons2(String type) {
		InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
		List < List < InlineKeyboardButton >> buttons = new ArrayList<>();
		List < InlineKeyboardButton > button_list1 = new ArrayList<>();
		List < InlineKeyboardButton > button_list2 = new ArrayList<>();
		List < InlineKeyboardButton > button_list3 = new ArrayList<>();

		InlineKeyboardButton england = new InlineKeyboardButton();
		england.setText("Premier League");
		england.setCallbackData("England_" + type);
		InlineKeyboardButton spain = new InlineKeyboardButton();
		spain.setText("Laliga Santander");
		spain.setCallbackData("Spain_" + type);
		InlineKeyboardButton italy = new InlineKeyboardButton();
		italy.setText("Serie A");
		italy.setCallbackData("Italy_" + type);
		InlineKeyboardButton germany = new InlineKeyboardButton();
		germany.setText("Bundesliga");
		germany.setCallbackData("Germany_" + type);
		InlineKeyboardButton france = new InlineKeyboardButton();
		france.setText("Ligue 1");
		france.setCallbackData("France_" + type);
		InlineKeyboardButton vietnam = new InlineKeyboardButton();
		vietnam.setText("V-League");
		vietnam.setCallbackData("Vietnam_" + type);
		
		button_list1.add(england);
		button_list1.add(spain);
		button_list2.add(italy);
		button_list2.add(germany);
		button_list3.add(france);
		button_list3.add(vietnam);
		
		buttons.add(button_list1);
		buttons.add(button_list2);
		buttons.add(button_list3);
		markupInline.setKeyboard(buttons);
		
		return markupInline;
	}
}
